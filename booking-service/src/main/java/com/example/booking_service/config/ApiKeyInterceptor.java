package com.example.booking_service.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {    
    private final Environment environment;

    public ApiKeyInterceptor(Environment environment) {
        this.environment = environment;
    }    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Checking API key for request: " + request.getRequestURI());
        String providedApiKey = request.getHeader("X-API-Key");
        String expectedApiKey = environment.getProperty("API_KEY");
        
        if (providedApiKey == null || expectedApiKey == null || !providedApiKey.equals(expectedApiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid or missing API key");
            return false;
        }
        
        return true;
    }
}
