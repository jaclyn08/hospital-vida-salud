package pe.edu.cibertec.hospital.citas.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.hospital.citas.event.model.CitaConfirmadaEvent;

@Component
public class AuditoriaConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaConsumer.class);

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public AuditoriaConsumer(DatabaseClient databaseClient, ObjectMapper objectMapper) {
        this.databaseClient = databaseClient;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${hospital.rabbitmq.queue.auditoria:hospital.citas.auditoria.queue}")
    public void auditarEvento(CitaConfirmadaEvent event) {
        log.info("[RABBITMQ LISTENER - AUDITORIA] Mensaje recibido exitosamente desde cola 'hospital.citas.auditoria.queue': [EventoId: {}, CitaId: {}]",
                event.getEventoId(), event.getCitaId());
        try {
            String payloadJson = objectMapper.writeValueAsString(event);
            databaseClient.sql("INSERT INTO auditoria_eventos (tipo_evento, origen_servicio, payload_json) VALUES (:tipo, :origen, :payload)")
                    .bind("tipo", event.getTipoEvento() != null ? event.getTipoEvento() : "CITA_CONFIRMADA")
                    .bind("origen", "citas-service")
                    .bind("payload", payloadJson)
                    .then()
                    .block();
            log.info("[AUDITORIA PERSISTENCIA] Evento {} persistido en base de datos (auditoria_eventos)", event.getEventoId());
        } catch (Exception e) {
            log.error("Error al auditar evento en base de datos: {}", e.getMessage());
        }
    }

}
