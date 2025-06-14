package com.example.booking_service.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.booking_service.util.JwtUtil;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.booking_service.dto.ErrorResponse;
import com.example.booking_service.exception.JwtAuthenticationException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil tokenValidator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthFilter(JwtUtil tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        // Skip JWT validation for public endpoints
        return path.startsWith("/api/bookings/reference/") ||
               (path.equals("/api/bookings") && method.equals("POST")) ||
               path.startsWith("/api/quotations"); // Allow all quotation endpoints without JWT
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException, JwtAuthenticationException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ErrorResponse errorResponse = new ErrorResponse(
            "No Authorization header",
            "No Authorization header found in the request",
            System.currentTimeMillis()
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {
            String token = authHeader.substring(7);
            // log.info("Processing JWT token: {}", token);
            if (tokenValidator.isTokenValid(token)) {
                String username = tokenValidator.extractUsername(token);
                var authorities = tokenValidator.extractAuthorities(token);
                log.info("Authenticated user: {}", username);
                var authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
                );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            
            ErrorResponse errorResponse = new ErrorResponse(
                "UNAUTHORIZED",
                "Invalid or expired JWT token",
                System.currentTimeMillis()
            );
            
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}