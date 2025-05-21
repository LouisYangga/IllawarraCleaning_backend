package com.example.booking_service.dto;

import java.time.LocalDateTime;
import com.example.booking_service.entity.Address;
import com.example.booking_service.entity.BookingStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    private String serviceType;
    private BookingStatus status;
    private double duration;
    private double price;
    private String notes;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
