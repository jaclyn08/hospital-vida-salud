package pe.edu.cibertec.hospital.citas.event.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.hospital.citas.event.model.CitaConfirmadaEvent;

@Component
public class CitaEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(CitaEventConsumer.class);

    @RabbitListener(queues = "${hospital.rabbitmq.queue.notificaciones:hospital.notificaciones.queue}")
    public void recibirNotificacionCitaConfirmada(CitaConfirmadaEvent event) {
        log.info("[MODULO NOTIFICACIONES] Mensaje recibido desde RabbitMQ para paciente ID {}: Cita {} ha sido confirmada con exito para fecha {}.",
                event.getPacienteId(), event.getCodigoCita(), event.getFechaHora());
    }

    @RabbitListener(queues = "${hospital.rabbitmq.queue.auditoria:hospital.auditoria.queue}")
    public void registrarAuditoriaCitaConfirmada(CitaConfirmadaEvent event) {
        log.info("[MODULO AUDITORIA] Traza de auditoria registrada: Evento ID {}, Cita {}, Especialidad {}, Estado: {}.",
                event.getEventoId(), event.getCodigoCita(), event.getEspecialidad(), event.getEstado());
    }

}
