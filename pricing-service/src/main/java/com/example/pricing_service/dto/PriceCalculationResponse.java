package com.example.pricing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.pricing_service.entity.AddOns;
import com.example.pricing_service.entity.ServiceType;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculationResponse {
    private Double price;
    private ServiceType serviceType;
    private Set<AddOns> addons;
    private Double duration;
    private String calculationDetails;
}
