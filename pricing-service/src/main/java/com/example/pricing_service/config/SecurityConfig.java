package com.example.pricing_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            // Public GET endpoints
            .requestMatchers("/api/prices/calculate").permitAll()
            .requestMatchers("/api/prices/services").permitAll()
            .requestMatchers("/api/prices/addons").permitAll()
            // Protect update, delete, and insert for services and addons
            .requestMatchers(HttpMethod.PUT, "/api/prices/services/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/prices/services/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/prices/services/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/prices/addons/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/prices/addons/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/prices/addons/**").hasRole("ADMIN")
            // Protect all other price-related endpoints
            .requestMatchers("/api/prices/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Since we're not using passwords
    }
}