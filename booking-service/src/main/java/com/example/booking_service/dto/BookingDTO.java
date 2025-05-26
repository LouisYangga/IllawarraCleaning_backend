package com.example.booking_service.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.booking_service.entity.Address;
import com.example.booking_service.entity.AddOns;
import com.example.booking_service.entity.BookingStatus;
import com.example.booking_service.entity.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private String userEmail;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private LocalDateTime scheduledAt;
    private ServiceType serviceType;
    private BookingStatus status;
    private double duration;
    private double price;
    private String notes;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<AddOns> addons = new HashSet<>();
}
