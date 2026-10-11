package pe.edu.cibertec.hospital.citas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.hospital.citas.client.PacienteClient;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCambioEstadoRequest;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCreateRequest;
import pe.edu.cibertec.hospital.citas.dto.response.CitaResponseDTO;
import pe.edu.cibertec.hospital.citas.entity.Cita;
import pe.edu.cibertec.hospital.citas.event.model.CitaConfirmadaEvent;
import pe.edu.cibertec.hospital.citas.event.publisher.CitaEventPublisher;
import pe.edu.cibertec.hospital.citas.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.hospital.citas.exception.ReglaNegocioException;
import pe.edu.cibertec.hospital.citas.exception.TransicionEstadoInvalidaException;
import pe.edu.cibertec.hospital.citas.repository.CitaRepository;
import pe.edu.cibertec.hospital.citas.service.CitaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CitaServiceImpl implements CitaService {

    private static final Logger log = LoggerFactory.getLogger(CitaServiceImpl.class);

    private final CitaRepository citaRepository;
    private final PacienteClient pacienteClient;
    private final CitaEventPublisher citaEventPublisher;

    public CitaServiceImpl(CitaRepository citaRepository, PacienteClient pacienteClient, CitaEventPublisher citaEventPublisher) {
        this.citaRepository = citaRepository;
        this.pacienteClient = pacienteClient;
        this.citaEventPublisher = citaEventPublisher;
    }

    @Override
    @Transactional
    public Mono<CitaResponseDTO> registrarCita(CitaCreateRequest request) {
        log.info("Iniciando registro reactivo de cita: {}", request.getCodigoCita());

        // 1. Validacion sincrona reactiva de existencia del paciente (RN-02 -> 400 Bad Request si no existe)
        return pacienteClient.obtenerPacientePorId(request.getPacienteId())
                .switchIfEmpty(Mono.error(new ReglaNegocioException("El paciente especificado no existe o no se encuentra activo.")))
                // 2. Validar que no exista ya una cita con el mismo codigo
                .flatMap(paciente -> citaRepository.findByCodigoCita(request.getCodigoCita())
                        .flatMap(existe -> Mono.<Void>error(new ReglaNegocioException("Ya existe una cita registrada con el codigo: " + request.getCodigoCita())))
                        .then()
                )
                // 3. Validar disponibilidad del profesional en la fecha y hora
                .then(Mono.defer(() -> citaRepository.findByProfesionalCodigoAndFechaHora(request.getProfesionalCodigo(), request.getFechaHora())
                        .flatMap(conflicto -> Mono.<Void>error(new ReglaNegocioException(
                                "El profesional " + request.getProfesionalCodigo() + " ya cuenta con una cita en el horario " + request.getFechaHora())))
                        .then()
                ))
                // 4. Validar que el paciente no tenga cita simultanea
                .then(Mono.defer(() -> citaRepository.existsByPacienteIdAndFechaHora(request.getPacienteId(), request.getFechaHora())
                        .flatMap(existe -> {
                            if (Boolean.TRUE.equals(existe)) {
                                return Mono.error(new ReglaNegocioException("El paciente ya tiene una cita agendada para esa misma fecha y hora."));
                            }
                            return Mono.empty();
                        })
                ))
                // 5. Construccion y persistencia reactiva en R2DBC
                .then(Mono.defer(() -> {
                    LocalDateTime ahora = LocalDateTime.now();
                    Cita nuevaCita = Cita.builder()
                            .codigoCita(request.getCodigoCita())
                            .pacienteId(request.getPacienteId())
                            .especialidad(request.getEspecialidad())
                            .fechaHora(request.getFechaHora())
                            .profesionalCodigo(request.getProfesionalCodigo())
                            .motivo(request.getMotivo())
                            .estado("PENDIENTE")
                            .fechaCreacion(ahora)
                            .fechaActualizacion(ahora)
                            .build();

                    return citaRepository.save(nuevaCita);
                }))
                .map(this::mapToResponseDTO);
    }

    @Override
    public Flux<CitaResponseDTO> listarCitas() {
        return citaRepository.findAll()
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<CitaResponseDTO> obtenerCitaPorId(Long id) {
        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Cita no encontrada con ID: " + id)))
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<CitaResponseDTO> obtenerCitaPorCodigo(String codigoCita) {
        return citaRepository.findByCodigoCita(codigoCita)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Cita no encontrada con codigo: " + codigoCita)))
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public Mono<CitaResponseDTO> confirmarCita(Long id) {
        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Cita no encontrada con ID: " + id)))
                .flatMap(cita -> {
                    String estadoActual = cita.getEstado();

                    // Regla RN-04: Solo citas PENDIENTE pueden ser CONFIRMADAS -> 422 Unprocessable Entity
                    if (!"PENDIENTE".equalsIgnoreCase(estadoActual)) {
                        return Mono.error(new TransicionEstadoInvalidaException(
                                "No se puede confirmar una cita en estado: " + estadoActual + ". Solo se admiten citas en estado PENDIENTE (RN-04)."));
                    }

                    cita.setEstado("CONFIRMADA");
                    cita.setFechaActualizacion(LocalDateTime.now());

                    return citaRepository.save(cita)
                            .flatMap(citaGuardada -> {
                                // Disparo de evento a RabbitMQ
                                CitaConfirmadaEvent event = CitaConfirmadaEvent.builder()
                                        .eventoId(UUID.randomUUID().toString())
                                        .tipoEvento("CITA_CONFIRMADA")
                                        .citaId(citaGuardada.getId())
                                        .pacienteId(citaGuardada.getPacienteId())
                                        .especialidad(citaGuardada.getEspecialidad())
                                        .fechaHora(citaGuardada.getFechaHora().toString())
                                        .timestamp(LocalDateTime.now().toString())
                                        .build();

                                return citaEventPublisher.publicarCitaConfirmada(event)
                                        .thenReturn(citaGuardada);
                            });
                })
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public Mono<CitaResponseDTO> cancelarCita(Long id) {
        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Cita no encontrada con ID: " + id)))
                .flatMap(cita -> {
                    String estadoActual = cita.getEstado();

                    // Regla RN-04
                    if ("CANCELADA".equalsIgnoreCase(estadoActual) || "ATENDIDA".equalsIgnoreCase(estadoActual)) {
                        return Mono.error(new TransicionEstadoInvalidaException(
                                "No se puede cancelar una cita en estado: " + estadoActual + " (RN-04)"));
                    }

                    cita.setEstado("CANCELADA");
                    cita.setFechaActualizacion(LocalDateTime.now());
                    return citaRepository.save(cita);
                })
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public Mono<CitaResponseDTO> cambiarEstadoCita(Long id, CitaCambioEstadoRequest request) {
        String nuevoEstado = request.getNuevoEstado().trim().toUpperCase();
        if ("CONFIRMADA".equalsIgnoreCase(nuevoEstado)) {
            return confirmarCita(id);
        } else if ("CANCELADA".equalsIgnoreCase(nuevoEstado)) {
            return cancelarCita(id);
        }

        return citaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Cita no encontrada con ID: " + id)))
                .flatMap(cita -> {
                    cita.setEstado(nuevoEstado);
                    cita.setFechaActualizacion(LocalDateTime.now());
                    return citaRepository.save(cita);
                })
                .map(this::mapToResponseDTO);
    }

    private CitaResponseDTO mapToResponseDTO(Cita cita) {
        return CitaResponseDTO.builder()
                .id(cita.getId())
                .codigoCita(cita.getCodigoCita())
                .pacienteId(cita.getPacienteId())
                .especialidad(cita.getEspecialidad())
                .fechaHora(cita.getFechaHora())
                .profesionalCodigo(cita.getProfesionalCodigo())
                .motivo(cita.getMotivo())
                .estado(cita.getEstado())
                .fechaCreacion(cita.getFechaCreacion())
                .fechaActualizacion(cita.getFechaActualizacion())
                .mensaje("Operacion completada con exito")
                .build();
    }

}
