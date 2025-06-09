package com.example.pricing_service.config;

import com.example.pricing_service.dto.ErrorResponse;
import com.example.pricing_service.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // Skip JWT validation for public endpoints
        return (path.equals("/api/prices/calculate") && "POST".equals(method)) ||
               (path.equals("/api/prices/services") && "GET".equals(method)) ||
               (path.startsWith("/api/prices/services/") && "GET".equals(method)) ||
               (path.equals("/api/prices/addons") && "GET".equals(method)) ||
               (path.startsWith("/api/prices/addons/") && "GET".equals(method));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        
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
            if (jwtUtil.isTokenValid(token)) {
                String username = jwtUtil.extractUsername(token);
                var authorities = jwtUtil.extractAuthorities(token);
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
