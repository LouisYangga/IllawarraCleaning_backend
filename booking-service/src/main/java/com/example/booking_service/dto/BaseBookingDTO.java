package com.example.booking_service.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class BaseBookingDTO {
    private String reference;
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
    private List<AddOns> addons = new ArrayList();
}
