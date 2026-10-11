package pe.edu.cibertec.hospital.citas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.hospital.citas.client.PacienteClient;
import pe.edu.cibertec.hospital.citas.client.dto.PacienteDTO;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCambioEstadoRequest;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCreateRequest;
import pe.edu.cibertec.hospital.citas.entity.Cita;
import pe.edu.cibertec.hospital.citas.event.publisher.CitaEventPublisher;
import pe.edu.cibertec.hospital.citas.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.hospital.citas.exception.ReglaNegocioException;
import pe.edu.cibertec.hospital.citas.exception.TransicionEstadoInvalidaException;
import pe.edu.cibertec.hospital.citas.repository.CitaRepository;
import pe.edu.cibertec.hospital.citas.service.impl.CitaServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private CitaEventPublisher citaEventPublisher;

    @InjectMocks
    private CitaServiceImpl citaService;

    private Cita citaMock;
    private CitaCreateRequest requestMock;

    @BeforeEach
    void setUp() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(2);
        citaMock = Cita.builder()
                .id(1L)
                .codigoCita("CIT-1001")
                .pacienteId(10L)
                .especialidad("Cardiologia")
                .fechaHora(fecha)
                .profesionalCodigo("MED-001")
                .motivo("Chequeo preventivo")
                .estado("PENDIENTE")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        requestMock = CitaCreateRequest.builder()
                .codigoCita("CIT-1001")
                .pacienteId(10L)
                .especialidad("Cardiologia")
                .fechaHora(fecha)
                .profesionalCodigo("MED-001")
                .motivo("Chequeo preventivo")
                .build();
    }

    @Test
    @DisplayName("Debe registrar una cita exitosamente con flujo reactivo Mono")
    void registrarCita_Exitoso() {
        PacienteDTO pacienteDTO = PacienteDTO.builder().id(10L).nombres("Juan").estado("ACTIVO").build();

        when(pacienteClient.obtenerPacientePorId(10L)).thenReturn(Mono.just(pacienteDTO));
        when(citaRepository.findByCodigoCita("CIT-1001")).thenReturn(Mono.empty());
        when(citaRepository.findByProfesionalCodigoAndFechaHora(any(), any())).thenReturn(Mono.empty());
        when(citaRepository.existsByPacienteIdAndFechaHora(any(), any())).thenReturn(Mono.just(false));
        when(citaRepository.save(any(Cita.class))).thenReturn(Mono.just(citaMock));

        StepVerifier.create(citaService.registrarCita(requestMock))
                .expectNextMatches(dto -> dto.getCodigoCita().equals("CIT-1001") && dto.getEstado().equals("PENDIENTE"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar al registrar cita si el codigo de cita ya existe")
    void registrarCita_CodigoDuplicado_Falla() {
        PacienteDTO pacienteDTO = PacienteDTO.builder().id(10L).nombres("Juan").estado("ACTIVO").build();

        when(pacienteClient.obtenerPacientePorId(10L)).thenReturn(Mono.just(pacienteDTO));
        when(citaRepository.findByCodigoCita("CIT-1001")).thenReturn(Mono.just(citaMock));

        StepVerifier.create(citaService.registrarCita(requestMock))
                .expectError(ReglaNegocioException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe confirmar cita y publicar evento asincrono RabbitMQ")
    void cambiarEstadoCita_Confirmada_PublicaEvento() {
        when(citaRepository.findById(1L)).thenReturn(Mono.just(citaMock));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            return Mono.just(c);
        });
        when(citaEventPublisher.publicarCitaConfirmada(any())).thenReturn(Mono.empty());

        CitaCambioEstadoRequest req = CitaCambioEstadoRequest.builder()
                .nuevoEstado("CONFIRMADA")
                .build();

        StepVerifier.create(citaService.cambiarEstadoCita(1L, req))
                .expectNextMatches(dto -> dto.getEstado().equals("CONFIRMADA"))
                .verifyComplete();

        verify(citaEventPublisher, times(1)).publicarCitaConfirmada(any());
    }

    @Test
    @DisplayName("Debe rechazar cambio de estado si la cita ya estaba cancelada")
    void cambiarEstadoCita_CitaCancelada_Rechaza() {
        citaMock.setEstado("CANCELADA");
        when(citaRepository.findById(1L)).thenReturn(Mono.just(citaMock));

        CitaCambioEstadoRequest req = CitaCambioEstadoRequest.builder()
                .nuevoEstado("CONFIRMADA")
                .build();

        StepVerifier.create(citaService.cambiarEstadoCita(1L, req))
                .expectErrorMatches(ex -> ex instanceof TransicionEstadoInvalidaException &&
                        ex.getMessage().contains("No se puede confirmar una cita en estado"))
                .verify();
    }

    @Test
    @DisplayName("Debe rechazar cancelar cita si ya estaba cancelada")
    void cancelarCita_YaCancelada_Rechaza() {
        citaMock.setEstado("CANCELADA");
        when(citaRepository.findById(1L)).thenReturn(Mono.just(citaMock));

        StepVerifier.create(citaService.cancelarCita(1L))
                .expectErrorMatches(ex -> ex instanceof TransicionEstadoInvalidaException &&
                        ex.getMessage().contains("No se puede cancelar una cita en estado"))
                .verify();
    }

    @Test
    void algunTestExistente() {

        org.junit.jupiter.api.Assertions.assertTrue(false, "Fallo provocado para evidencia 7.16");
    }

}
