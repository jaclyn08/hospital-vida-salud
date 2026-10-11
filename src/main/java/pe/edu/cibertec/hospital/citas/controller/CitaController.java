package pe.edu.cibertec.hospital.citas.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCambioEstadoRequest;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCreateRequest;
import pe.edu.cibertec.hospital.citas.dto.response.CitaResponseDTO;
import pe.edu.cibertec.hospital.citas.service.CitaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CitaResponseDTO> registrarCita(@Valid @RequestBody CitaCreateRequest request) {
        return citaService.registrarCita(request);
    }

    @GetMapping
    public Flux<CitaResponseDTO> listarCitas() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public Mono<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    @GetMapping("/codigo/{codigoCita}")
    public Mono<CitaResponseDTO> obtenerCitaPorCodigo(@PathVariable String codigoCita) {
        return citaService.obtenerCitaPorCodigo(codigoCita);
    }

    @PutMapping("/{id}/confirmar")
    public Mono<CitaResponseDTO> confirmarCita(@PathVariable Long id) {
        return citaService.confirmarCita(id);
    }

    @PutMapping("/{id}/cancelar")
    public Mono<CitaResponseDTO> cancelarCita(@PathVariable Long id) {
        return citaService.cancelarCita(id);
    }

    @PatchMapping("/{id}/estado")
    public Mono<CitaResponseDTO> cambiarEstadoCita(@PathVariable Long id,
                                                   @Valid @RequestBody CitaCambioEstadoRequest request) {
        return citaService.cambiarEstadoCita(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<CitaResponseDTO> eliminarCita(@PathVariable Long id) {
        return citaService.cancelarCita(id);
    }

}
