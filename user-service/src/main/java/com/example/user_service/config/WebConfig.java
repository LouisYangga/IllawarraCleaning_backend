package com.example.user_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;
    private final JwtAuthInterceptor jwtAuthInterceptor;
    public WebConfig(ApiKeyInterceptor apiKeyInterceptor, JwtAuthInterceptor jwtAuthInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**") // This will protect all paths under /api/
                .excludePathPatterns("/api/health"); // Example of excluding health check endpoint
        
            registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/admin/**")
                .addPathPatterns("/api/users/**")
                .excludePathPatterns("/api/admin/login")
                .excludePathPatterns("/api/users/update/**")
                .excludePathPatterns("/api/users/email/**");
    
            }
}
