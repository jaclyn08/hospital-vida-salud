package pe.edu.cibertec.hospital.citas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.hospital.citas.client.dto.PacienteDTO;
import pe.edu.cibertec.hospital.citas.exception.DocumentoDuplicadoException;
import pe.edu.cibertec.hospital.citas.exception.RecursoNoEncontradoException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteMockController {

    private final Map<Long, PacienteDTO> pacientesDb = new ConcurrentHashMap<>();
    private final Map<String, Long> documentosDb = new ConcurrentHashMap<>();

    public PacienteMockController() {
        PacienteDTO p1 = PacienteDTO.builder()
                .id(1L)
                .tipoDocumento("DNI")
                .numeroDocumento("12345678")
                .nombres("Carlos")
                .apellidos("Mendoza")
                .estado("ACTIVO")
                .build();
        pacientesDb.put(1L, p1);
        documentosDb.put("12345678", 1L);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PacienteDTO> registrarPaciente(@RequestBody PacienteDTO request) {
        if (request.getNumeroDocumento() != null && documentosDb.containsKey(request.getNumeroDocumento())) {
            return Mono.error(new DocumentoDuplicadoException(
                    "El paciente con documento " + request.getNumeroDocumento() + " ya se encuentra registrado."));
        }

        long nuevoId = pacientesDb.size() + 1L;
        request.setId(nuevoId);
        if (request.getEstado() == null) {
            request.setEstado("ACTIVO");
        }

        pacientesDb.put(nuevoId, request);
        if (request.getNumeroDocumento() != null) {
            documentosDb.put(request.getNumeroDocumento(), nuevoId);
        }

        return Mono.just(request);
    }

    @GetMapping("/{id}")
    public Mono<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        PacienteDTO paciente = pacientesDb.get(id);
        if (paciente == null) {
            return Mono.error(new RecursoNoEncontradoException("El paciente con ID " + id + " no existe."));
        }
        return Mono.just(paciente);
    }

}
