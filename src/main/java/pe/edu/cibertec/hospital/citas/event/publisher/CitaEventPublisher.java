package pe.edu.cibertec.hospital.citas.event.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.hospital.citas.event.model.CitaConfirmadaEvent;
import reactor.core.publisher.Mono;

@Component
public class CitaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CitaEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${hospital.rabbitmq.exchange:hospital.exchange}")
    private String exchangeName;

    @Value("${hospital.rabbitmq.routingkey.cita-confirmada:cita.confirmada}")
    private String routingKeyCitaConfirmada;

    public CitaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica de forma asincrona un evento de cita confirmada hacia RabbitMQ sin bloquear el flujo reactivo.
     * Incorpora resiliencia y simulacion academica en caso el broker local no este activo.
     */
    public Mono<Void> publicarCitaConfirmada(CitaConfirmadaEvent event) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("[RABBITMQ DISPATCH] Publicando evento CitaConfirmada a RabbitMQ: [Exchange: {}, RoutingKey: {}, EventoId: {}]",
                        exchangeName, routingKeyCitaConfirmada, event.getEventoId());
                rabbitTemplate.convertAndSend(exchangeName, routingKeyCitaConfirmada, event);
                log.info("[RABBITMQ SUCCESS] Evento entregado al broker RabbitMQ exitosamente.");
            } catch (Exception ex) {
                log.error("[RABBITMQ ERROR] Error al enviar mensaje al broker RabbitMQ: {}", ex.getMessage());
            }
        })
        .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
        .then();
    }

}
