package com.example.booking_service.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {
    @Bean
    public Caffeine<Object, Object> caffeineCacheSpec() {
        return Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES) // Quotation expires after 30 minutes
                .maximumSize(10000);
    }
}
