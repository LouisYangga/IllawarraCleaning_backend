package com.example.pricing_service.dto;

import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.example.pricing_service.entity.ServiceType;
import com.example.pricing_service.entity.AddOns;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationEvent {
    private String quotationId;
    private ServiceType serviceType;
    private Set<AddOns> addons;
    private double duration;
    private Double price;
    private String status;
    private String errorMessage;
}