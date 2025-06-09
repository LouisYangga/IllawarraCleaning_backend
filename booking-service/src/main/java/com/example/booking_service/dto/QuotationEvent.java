package com.example.booking_service.dto;

import com.example.booking_service.entity.ServiceType;
import com.example.booking_service.entity.AddOns;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

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
}
