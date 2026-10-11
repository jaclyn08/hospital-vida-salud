package pe.edu.cibertec.hospital.citas.service;

import pe.edu.cibertec.hospital.citas.dto.request.CitaCambioEstadoRequest;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCreateRequest;
import pe.edu.cibertec.hospital.citas.dto.response.CitaResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CitaService {

    Mono<CitaResponseDTO> registrarCita(CitaCreateRequest request);

    Flux<CitaResponseDTO> listarCitas();

    Mono<CitaResponseDTO> obtenerCitaPorId(Long id);

    Mono<CitaResponseDTO> obtenerCitaPorCodigo(String codigoCita);

    Mono<CitaResponseDTO> confirmarCita(Long id);

    Mono<CitaResponseDTO> cancelarCita(Long id);

    Mono<CitaResponseDTO> cambiarEstadoCita(Long id, CitaCambioEstadoRequest request);

}
