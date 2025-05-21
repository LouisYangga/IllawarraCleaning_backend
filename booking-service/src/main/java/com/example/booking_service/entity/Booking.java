package com.example.booking_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email is required")
    private String userEmail;
    private String firstName;
    private String lastName;
    private Long phoneNumber;

    @NotNull(message = "Service date is required")
    private LocalDateTime scheduledAt;
    private String serviceType;
    private BookingStatus status;  // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private double duration; //in hours
    private double price; //in dollars
    private String notes;
    
    @Embedded
    @NotNull(message = "Address is required")
    private Address address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
