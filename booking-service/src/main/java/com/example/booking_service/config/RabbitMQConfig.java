package com.example.booking_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.user.name}")
    private String userQueueName;

    @Value("${rabbitmq.queue.quotation.name}")
    private String quotationQueueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.user.key}")
    private String userRoutingKey;

    @Value("${rabbitmq.routing.quotation.key}")
    private String quotationRoutingKey;

    @Bean
    public Queue userQueue() {
        return new Queue(userQueueName, true);
    }

    @Bean
    public Queue quotationQueue() {
        return new Queue(quotationQueueName, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding userBinding() {
        return BindingBuilder
                .bind(userQueue())
                .to(exchange())
                .with(userRoutingKey);
    }

    @Bean
    public Binding quotationBinding() {
        return BindingBuilder
                .bind(quotationQueue())
                .to(exchange())
                .with(quotationRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setReplyTimeout(60000); // 60 seconds timeout for replies
        template.setMandatory(true);     // Ensure messages reach a queue
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        
        // Configure concurrent consumers
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        
        // Configure prefetch count
        factory.setPrefetchCount(1);
        
        // Configure error handling
        factory.setDefaultRequeueRejected(false);
        factory.setErrorHandler(throwable -> {
            log.error("Error in RabbitMQ listener: {}", throwable.getMessage());
        });
        
        return factory;
    }
}