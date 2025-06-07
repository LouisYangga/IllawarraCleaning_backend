package com.example.booking_service.service;

import com.example.booking_service.dto.QuotationEvent;
import com.example.booking_service.dto.QuotationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuotationEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String quotationRoutingKey;
    private final String exchangeName;

    public QuotationEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.routing.quotation.key}") String quotationRoutingKey,
            @Value("${rabbitmq.exchange.name}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.quotationRoutingKey = quotationRoutingKey;
        this.exchangeName = exchangeName;
    }

    public QuotationResponse publishQuotationEvent(QuotationEvent event) {
        log.info("Publishing quotation calculation request for ID: {}", event.getQuotationId());
        
        QuotationResponse reply = (QuotationResponse) rabbitTemplate.convertSendAndReceive(
            exchangeName,
            quotationRoutingKey,
            event
        );

        if (reply == null) {
            log.error("Failed to get price calculation: No response from pricing service");
            throw new RuntimeException("Failed to calculate price");
        }

        log.info("Received price calculation for quotation {}: ${}", event.getQuotationId(), reply.getPrice());
        return reply;
    }
}
