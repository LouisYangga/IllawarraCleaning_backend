package com.example.pricing_service.dto;

import com.example.pricing_service.entity.AddOns;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddonPriceDTO {
    @NotNull
    private AddOns addon;
    
    @NotNull
    @Positive
    private Double price;
    
    private String description;
}
