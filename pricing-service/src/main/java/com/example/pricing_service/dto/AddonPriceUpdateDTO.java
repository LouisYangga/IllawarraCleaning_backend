package com.example.pricing_service.dto;

import com.example.pricing_service.entity.AddOns;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddonPriceUpdateDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddOns addon;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double price;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
}
