package com.example.pricing_service.dto;

import com.example.pricing_service.entity.ServiceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicePriceDTO {
    @NotNull
    private ServiceType serviceType;
    
    @NotNull
    @Positive
    private double basePrice;
    
    @NotNull
    @Positive
    private double hourlyRate;
    
    private String description;
}
