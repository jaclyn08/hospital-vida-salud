package pe.edu.cibertec.hospital.citas.event.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.hospital.citas.event.model.CitaConfirmadaEvent;

@Component
public class NotificacionConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacionConsumer.class);

    @RabbitListener(queues = "${hospital.rabbitmq.queue.notificaciones:hospital.citas.notificaciones.queue}")
    public void recibirNotificacion(CitaConfirmadaEvent event) {
        log.info("[RABBITMQ LISTENER - NOTIFICACIONES] Mensaje recibido desde cola 'hospital.citas.notificaciones.queue': [CitaId: {}, PacienteId: {}, Especialidad: {}]",
                event.getCitaId(), event.getPacienteId(), event.getEspecialidad());
        log.info("[RABBITMQ LISTENER - NOTIFICACIONES] Notificacion enviada al paciente ID: {} para la cita ID: {}",
                event.getPacienteId(), event.getCitaId());
    }

}
