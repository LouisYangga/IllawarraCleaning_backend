package com.example.booking_service.service;

import com.example.booking_service.dto.QuotationRequest;
import com.example.booking_service.dto.QuotationResponse;
import com.example.booking_service.dto.QuotationEvent;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
public class QuotationService {
    private final Cache<String, QuotationResponse> cache;
    private final QuotationEventPublisher eventPublisher;

    public QuotationService(Caffeine<Object, Object> caffeine, QuotationEventPublisher eventPublisher) {
        this.cache = caffeine.recordStats().build();
        this.eventPublisher = eventPublisher;
    }

    public QuotationResponse createQuotation(QuotationRequest request) {
        String quotationId = UUID.randomUUID().toString();
        
        // If addons is null or empty, set it to null so it won't be calculated
        var addons = (request.getAddons() == null || request.getAddons().isEmpty()) ? null : request.getAddons();
        
        QuotationEvent event = new QuotationEvent(
            quotationId,
            request.getServiceType(),
            addons,
            request.getDuration(),
            0.0,
            "PENDING"
        );

        QuotationResponse reply = eventPublisher.publishQuotationEvent(event);
        
        QuotationResponse response = new QuotationResponse(
            quotationId,
            reply.getPrice(),
            request.getServiceType(),
            request.getDuration(),
            request.getAddons()
        );
        
        cache.put(quotationId, response);
        log.info("Received price calculation for quotation {}: ${}", quotationId, reply.getPrice());
        return response;
    }

    public QuotationResponse getQuotation(String quotationId) {
        QuotationResponse response = cache.getIfPresent(quotationId);
        if (response == null) {
            throw new IllegalArgumentException("Quotation not found or has expired: " + quotationId);
        }
        return response;
    }
    /**
     * Returns cache statistics for monitoring
     */
    public String getCacheStats() {
        return cache.stats().toString();
    }
}
