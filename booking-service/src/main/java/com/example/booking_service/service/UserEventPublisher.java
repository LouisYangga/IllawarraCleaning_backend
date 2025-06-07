package com.example.booking_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.booking_service.dto.UserCreationEventDTO;

@Service
public class UserEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    
    @Value("${rabbitmq.routing.user.key}")
    private String routingKey;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserEvent(UserCreationEventDTO event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
    }
}
