package com.example.pricing_service.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import com.example.pricing_service.dto.PriceCalculationRequest;
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
    public QuotationEvent handleQuotationRequest(QuotationEvent event) {
        log.info("Received quotation calculation request for ID: {}", event.getQuotationId());
        
        try {
            PriceCalculationRequest request = new PriceCalculationRequest(
                event.getServiceType(),
                event.getAddons(),
                event.getDuration()
            );
            // Calculate total price including base price, duration-based cost, and addons
            double calculatedPrice = pricingService.calculatePrice(request);
            
            // Update event with calculated price
            event.setPrice(calculatedPrice);
            event.setStatus("COMPLETED");
            
            log.info("Completed price calculation for quotation {}: ${}", 
                    event.getQuotationId(), calculatedPrice);
            log.info("returning quotationevent");
            return event;
            
        } catch (Exception e) {
            log.error("Error calculating price for quotation {}: {}", 
                    event.getQuotationId(), e.getMessage());
            
            event.setStatus("FAILED");
            event.setErrorMessage(e.getMessage());
            return event;
        }
    }
}
