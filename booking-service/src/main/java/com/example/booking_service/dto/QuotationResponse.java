package com.example.booking_service.dto;

import java.util.List;


import com.example.booking_service.entity.AddOns;
import com.example.booking_service.entity.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationResponse {
    private String quotationId;
    private double price;
    private ServiceType serviceType;
    private double duration;
    private List<AddOns> addons;
}
