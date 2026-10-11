package pe.edu.cibertec.hospital.citas.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${hospital.rabbitmq.exchange:hospital.exchange}")
    private String exchangeName;

    @Value("${hospital.rabbitmq.queue.notificaciones:hospital.citas.notificaciones.queue}")
    private String notificacionesQueueName;

    @Value("${hospital.rabbitmq.queue.auditoria:hospital.citas.auditoria.queue}")
    private String auditoriaQueueName;

    @Value("${hospital.rabbitmq.routingkey.cita-confirmada:cita.confirmada}")
    private String routingKeyCitaConfirmada;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean("notificacionesQueue")
    public Queue notificacionesQueue() {
        return QueueBuilder.durable(notificacionesQueueName).build();
    }

    @Bean("auditoriaQueue")
    public Queue auditoriaQueue() {
        return QueueBuilder.durable(auditoriaQueueName).build();
    }

    @Bean
    public Binding bindingNotificaciones(@Qualifier("notificacionesQueue") Queue notificacionesQueue,
                                         TopicExchange topicExchange) {
        return BindingBuilder.bind(notificacionesQueue).to(topicExchange).with("cita.*");
    }

    @Bean
    public Binding bindingAuditoria(@Qualifier("auditoriaQueue") Queue auditoriaQueue,
                                    TopicExchange topicExchange) {
        return BindingBuilder.bind(auditoriaQueue).to(topicExchange).with("#");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory factory =
                new org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}
