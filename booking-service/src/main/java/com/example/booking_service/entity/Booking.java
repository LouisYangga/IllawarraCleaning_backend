package com.example.booking_service.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_sequence")
    @SequenceGenerator(name = "booking_sequence", sequenceName = "booking_seq", initialValue = 10000)
    private Long id;

    @NotNull(message = "Email is required")
    private String userEmail;
    private String firstName;
    private String lastName;
    private Long phoneNumber;

    @NotNull(message = "Service date is required")
    private LocalDateTime scheduledAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType; // END_OF_LEASE. DEEP_CLEANING, REGULAR_CLEANING
    @Enumerated(EnumType.STRING)
    private BookingStatus status;  // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private double duration; //in hours
    private double price; //in dollars
    private String notes;
    
    @Embedded
    @NotNull(message = "Address is required")
    private Address address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ElementCollection(targetClass = AddOns.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "booking_addons", joinColumns = @JoinColumn(name = "booking_id"))
    private Set<AddOns> addons = new HashSet<>();

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
