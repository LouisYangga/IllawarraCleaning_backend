package com.example.booking_service.service;

import com.example.booking_service.client.PriceFeignClient;
import com.example.booking_service.dto.QuotationRequest;
import com.example.booking_service.dto.QuotationResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class QuotationService {
    private final Cache<String, QuotationResponse> cache;
    private final PriceFeignClient priceFeignClient;

    public QuotationService(Caffeine<Object, Object> caffeine, PriceFeignClient priceFeignClient) {
        this.cache = caffeine.build();
        this.priceFeignClient = priceFeignClient;
    }

    public QuotationResponse createQuotation(QuotationRequest request) {
        double price = priceFeignClient.calculatePrice(
            request.getServiceType(), request.getAddons(), request.getDuration()
        );
        String quotationId = UUID.randomUUID().toString();
        QuotationResponse response = new QuotationResponse(quotationId, price,request.getServiceType(),
            request.getDuration(), request.getAddons());
        cache.put(quotationId, response);
        return response;
    }

    public QuotationResponse getQuotation(String quotationId) {
        QuotationResponse response = cache.getIfPresent(quotationId);
        if (response == null) {
            throw new IllegalArgumentException("Invalid or expired quotation ID.");
        }
        return response;
    }
}
