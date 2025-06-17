package com.example.booking_service.dto;

import com.example.booking_service.entity.ServiceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.booking_service.entity.AddOns;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationEvent {
    private String quotationId;
    private ServiceType serviceType;
    private List<AddOns> addons;
    private double duration;
    private Double price;
    private String status;
}
