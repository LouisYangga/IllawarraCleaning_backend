package com.example.booking_service.config;

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
                // Public endpoints
                .requestMatchers("/api/bookings/reference/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/bookings").permitAll()
                .requestMatchers("/api/quotations/**").permitAll()
                
                // Admin only endpoints
                .requestMatchers(HttpMethod.GET,"/api/bookings").hasRole("ADMIN")
                .requestMatchers("/api/bookings/status/**").hasRole("ADMIN")
                .requestMatchers("/api/bookings/date-range").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/bookings/*/status").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(basic -> basic.disable()) // Disable basic auth
            .formLogin(form -> form.disable());  // Disable form login
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Since we're not using passwords
    }
}
