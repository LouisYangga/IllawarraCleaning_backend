package com.example.pricing_service.dto;

import com.example.pricing_service.entity.ServiceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicePriceUpdateDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ServiceType serviceType;
      @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double basePrice;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double hourlyRate;
    
    private String description;
}
