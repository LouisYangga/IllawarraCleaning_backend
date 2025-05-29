package com.example.booking_service.dto;

import com.example.booking_service.entity.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.booking_service.entity.AddOns;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationRequest {
    private ServiceType serviceType;
    private Set<AddOns> addons;
    private double duration;
}
