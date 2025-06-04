package com.example.pricing_service.dto;

import com.example.pricing_service.entity.ServiceType;
import com.example.pricing_service.entity.AddOns;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculationRequest {
    @NotNull
    private ServiceType serviceType;

    private Set<AddOns> addons;

    @NotNull
    @Positive
    private double duration;
}
