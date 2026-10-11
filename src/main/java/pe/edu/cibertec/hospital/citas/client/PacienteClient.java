package pe.edu.cibertec.hospital.citas.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.hospital.citas.client.dto.PacienteDTO;
import pe.edu.cibertec.hospital.citas.exception.ReglaNegocioException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class PacienteClient {

    private static final Logger log = LoggerFactory.getLogger(PacienteClient.class);

    private final WebClient webClient;

    public PacienteClient(WebClient.Builder webClientBuilder,
                          @Value("${services.pacientes.url:http://localhost:8080}") String pacientesUrl) {
        this.webClient = webClientBuilder.baseUrl(pacientesUrl).build();
    }

    /**
     * Consulta sincrona reactiva al microservicio de pacientes con timeout y manejo de errores.
     */
    public Mono<PacienteDTO> obtenerPacientePorId(Long pacienteId) {
        log.info("Consultando de forma sincrona al servicio de pacientes para ID: {}", pacienteId);

        return webClient.get()
                .uri("/api/pacientes/{id}", pacienteId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ReglaNegocioException("El paciente con ID " + pacienteId + " no existe o no se encuentra activo (RN-02)."))
                )
                .bodyToMono(PacienteDTO.class)
                .timeout(Duration.ofSeconds(3));
    }

}
