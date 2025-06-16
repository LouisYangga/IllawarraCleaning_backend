package com.example.booking_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import com.example.booking_service.dto.QuotationRequest;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Service
@Slf4j
public class PricingServiceClient {
    private final RestTemplate restTemplate;
    private final String pricingServiceUrl;
    private final String apiKey;

    public PricingServiceClient(
            RestTemplate restTemplate,
            @Value("${services.pricing.url}") String pricingServiceUrl,
            @Value("${services.pricing.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.pricingServiceUrl = pricingServiceUrl;
        this.apiKey = apiKey;
    }

    public Double calculatePrice(QuotationRequest request) {
        try {
            log.info("Calculating price for request: {}", request);
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", apiKey);
            
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                pricingServiceUrl + "/api/prices/calculate",
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getBody() != null && response.getBody().containsKey("price")) {
                return ((Number) response.getBody().get("price")).doubleValue();
            }
            return null;
        } catch (Exception e) {
            log.error("Error calculating price: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate price", e);
        }
    }
}
