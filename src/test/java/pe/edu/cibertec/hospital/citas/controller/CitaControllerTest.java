package pe.edu.cibertec.hospital.citas.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCambioEstadoRequest;
import pe.edu.cibertec.hospital.citas.dto.request.CitaCreateRequest;
import pe.edu.cibertec.hospital.citas.dto.response.CitaResponseDTO;
import pe.edu.cibertec.hospital.citas.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.hospital.citas.service.CitaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CitaController.class)
class CitaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CitaService citaService;

    @Test
    @DisplayName("POST /api/citas debe responder 201 CREATED al registrar cita valida")
    void registrarCita_Retorna201() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(3);
        CitaCreateRequest request = CitaCreateRequest.builder()
                .codigoCita("CIT-2001")
                .pacienteId(15L)
                .especialidad("Pediatria")
                .fechaHora(fecha)
                .profesionalCodigo("DOC-10")
                .motivo("Consulta general")
                .build();

        CitaResponseDTO responseDTO = CitaResponseDTO.builder()
                .id(1L)
                .codigoCita("CIT-2001")
                .pacienteId(15L)
                .especialidad("Pediatria")
                .fechaHora(fecha)
                .profesionalCodigo("DOC-10")
                .estado("PENDIENTE")
                .mensaje("Operacion completada con exito")
                .build();

        when(citaService.registrarCita(any(CitaCreateRequest.class))).thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.codigoCita").isEqualTo("CIT-2001")
                .jsonPath("$.estado").isEqualTo("PENDIENTE");
    }

    @Test
    @DisplayName("GET /api/citas debe responder 200 OK y devolver flujo Flux de citas")
    void listarCitas_Retorna200() {
        CitaResponseDTO c1 = CitaResponseDTO.builder().id(1L).codigoCita("CIT-2001").estado("PENDIENTE").build();
        CitaResponseDTO c2 = CitaResponseDTO.builder().id(2L).codigoCita("CIT-2002").estado("CONFIRMADA").build();

        when(citaService.listarCitas()).thenReturn(Flux.just(c1, c2));

        webTestClient.get()
                .uri("/api/citas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CitaResponseDTO.class)
                .hasSize(2);
    }

    @Test
    @DisplayName("GET /api/citas/{id} debe retornar 404 NOT FOUND cuando la cita no existe")
    void obtenerCitaPorId_Inexistente_Retorna404() {
        when(citaService.obtenerCitaPorId(999L))
                .thenReturn(Mono.error(new RecursoNoEncontradoException("Cita no encontrada con ID: 999")));

        webTestClient.get()
                .uri("/api/citas/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.mensaje").isEqualTo("Cita no encontrada con ID: 999");
    }

    @Test
    @DisplayName("PATCH /api/citas/{id}/estado debe actualizar estado correctamente")
    void cambiarEstadoCita_Retorna200() {
        CitaCambioEstadoRequest req = CitaCambioEstadoRequest.builder()
                .nuevoEstado("CONFIRMADA")
                .build();

        CitaResponseDTO resp = CitaResponseDTO.builder()
                .id(1L)
                .codigoCita("CIT-2001")
                .estado("CONFIRMADA")
                .build();

        when(citaService.cambiarEstadoCita(eq(1L), any())).thenReturn(Mono.just(resp));

        webTestClient.patch()
                .uri("/api/citas/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.estado").isEqualTo("CONFIRMADA");
    }

    @Test
    @DisplayName("PUT /api/citas/{id}/confirmar debe responder 200 OK")
    void confirmarCita_Retorna200() {
        CitaResponseDTO resp = CitaResponseDTO.builder()
                .id(1L)
                .codigoCita("CIT-2001")
                .estado("CONFIRMADA")
                .build();

        when(citaService.confirmarCita(1L)).thenReturn(Mono.just(resp));

        webTestClient.put()
                .uri("/api/citas/1/confirmar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.estado").isEqualTo("CONFIRMADA");
    }

}
