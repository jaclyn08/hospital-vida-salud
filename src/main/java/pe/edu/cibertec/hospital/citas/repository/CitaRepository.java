package pe.edu.cibertec.hospital.citas.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.hospital.citas.entity.Cita;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface CitaRepository extends ReactiveCrudRepository<Cita, Long> {

    Mono<Cita> findByCodigoCita(String codigoCita);

    Flux<Cita> findByPacienteId(Long pacienteId);

    Mono<Cita> findByProfesionalCodigoAndFechaHora(String profesionalCodigo, LocalDateTime fechaHora);

    Mono<Boolean> existsByPacienteIdAndFechaHora(Long pacienteId, LocalDateTime fechaHora);

}
