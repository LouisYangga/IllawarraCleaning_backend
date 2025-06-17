package com.example.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;
    private final JwtAuthInterceptor jwtAuthInterceptor;
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    public WebConfig(ApiKeyInterceptor apiKeyInterceptor, JwtAuthInterceptor jwtAuthInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**") // This will protect all paths under /api/
                .excludePathPatterns("/api/users/health"); // Example of excluding health check endpoint
        
            registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/admin/**")
                .addPathPatterns("/api/users/**")
                .excludePathPatterns("/api/admin/login")
                .excludePathPatterns("/api/users/update/**")
                .excludePathPatterns("/api/users/email/**")
                .excludePathPatterns("/api/users/health");
    
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
