package com.example.pricing_service.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String providedApiKey = request.getHeader("X-API-Key");
        
        log.info("Expected API key: [{}]", apiKey);
        log.info("Provided API key: [{}]", providedApiKey);
        log.info("Request URI: {}", request.getRequestURI());
        
        if (providedApiKey == null || !providedApiKey.equals(apiKey)) {
            log.warn("API key validation failed");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String errorJson = "{\"error\": \"Invalid or missing API key\", \"status\": 401}";
            response.getWriter().write(errorJson);
            response.getWriter().flush();
            return false;
        }

        log.info("API key validation successful");
        return true;
    }
}
