package com.example.booking_service.dto;

import com.example.booking_service.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AddressValidationResult {
    private boolean valid;
    private String message;
    private String formattedAddress;
    private Address validatedAddress;
}