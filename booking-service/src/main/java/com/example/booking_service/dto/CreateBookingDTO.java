package com.example.booking_service.dto;

import java.time.LocalDateTime;

import com.example.booking_service.entity.Address;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDTO {
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String userEmail;
    
    private String firstName;
    private String lastName;
    @NotNull(message = "Phone number is required")
    private Long phoneNumber;
    
    @NotNull(message = "Service date is required")
    private LocalDateTime scheduledAt;
    
    private String serviceType;
    private double duration;
    private double price;
    private String notes;
    @NotNull(message = "Address is required")
    private Address address;
}
