package com.example.pricing_service.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import com.example.pricing_service.dto.PriceCalculationRequest;
import com.example.pricing_service.dto.PriceCalculationResponse;
import com.example.pricing_service.dto.QuotationEvent;
import com.example.pricing_service.service.PricingService;

@Component
@Slf4j
public class QuotationEventListener {

    private final PricingService pricingService;

    public QuotationEventListener(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.quotation.name}")
    public String handleQuotationRequest(QuotationEvent event) {
        log.info("Received quotation calculation request for ID: {}", event.getQuotationId());

        try {
            PriceCalculationRequest request = new PriceCalculationRequest(
                event.getServiceType(),
                event.getAddons(),
                event.getDuration()
            );
            // Calculate total price including base price, duration-based cost, and addons
            PriceCalculationResponse response = pricingService.calculatePrice(request);
            Double calculatedPrice = response.getPrice();
            // Update event with calculated price
            event.setPrice(calculatedPrice);
            event.setStatus("COMPLETED");

            log.info("Completed price calculation for quotation {}: ${}", 
                    event.getQuotationId(), calculatedPrice);
            log.info("returning quotationevent");

            // Convert event to JSON string
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(event);

        } catch (Exception e) {
            log.error("Error calculating price for quotation {}: {}", 
                    event.getQuotationId(), e.getMessage());

            event.setStatus("FAILED");
            event.setErrorMessage(e.getMessage());
            try {
                return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(event);
            } catch (Exception ex) {
                log.error("Error serializing error response: {}", ex.getMessage());
                return "{\"status\":\"FAILED\",\"errorMessage\":\"Serialization error\"}";
            }
        }
    }
}

