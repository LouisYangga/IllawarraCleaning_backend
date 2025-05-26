package com.example.booking_service.dto;

import java.time.LocalDateTime;
import com.example.booking_service.entity.Address;
import com.example.booking_service.entity.BookingStatus;
import com.example.booking_service.entity.ServiceType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingDTO {
    private Long id;
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
}
