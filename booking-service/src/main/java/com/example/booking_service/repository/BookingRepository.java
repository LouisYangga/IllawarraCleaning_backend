package com.example.booking_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_service.entity.Booking;
import com.example.booking_service.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserEmailAndScheduledAt(String userEmail, LocalDateTime scheduledAt);
    List<Booking> findByUserEmail(String userEmail);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);
    List<Booking> findByUserEmailAndStatus(String userEmail, BookingStatus status);
    boolean existsByUserEmailAndScheduledAt(String userEmail, LocalDateTime scheduledAt);
}
