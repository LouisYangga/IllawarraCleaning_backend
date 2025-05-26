package com.example.user_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.user_service.dto.CreateUserDTO;

@Service
public class UserEventConsumer {
    private final UserService userService;

    public UserEventConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.user.name}")
    public void handleUserCreationEvent(CreateUserDTO event) {
        CreateUserDTO dto = new CreateUserDTO(event.getFirstName(), event.getLastName(), event.getEmail(), event.getPhoneNumber());
        if (userService.existsByEmail(event.getEmail())) {
            userService.incrementBookingCount(event.getEmail());
        } else {
            userService.createUser(dto);
            userService.incrementBookingCount(event.getEmail());
        }
    }
}
