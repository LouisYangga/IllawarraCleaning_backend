package com.example.booking_service.util;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.booking_service.exception.JwtAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
    @Value("${JWT_SECRET}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        try {
            Claims claims = extractAllClaims(token);
            // Change from "roles" to "role"
            String role = (String) claims.get("role");
            if (role == null) {
                log.warn("No role found in token");
                return Collections.emptyList();
            }
            
            return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
            );
        } catch (Exception e) {
            log.error("Failed to extract authorities: {}", e.getMessage());
            throw new JwtAuthenticationException("Failed to extract authorities: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            // Also verify expiration
            if (claims.getExpiration().before(new Date())) {
                throw new JwtAuthenticationException("JWT token has expired");
            }
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT signature");
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw new JwtAuthenticationException("Token validation failed: " + e.getMessage());
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            // log.info("SECRET KEY: {}", secretKey);
            Key signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
            
            return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            log.error("Failed to extract claims: {}", e.getMessage());
            throw new JwtAuthenticationException("Failed to extract claims: " + e.getMessage());
        }
    }
}
