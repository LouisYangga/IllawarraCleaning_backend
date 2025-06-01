package com.example.booking_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.booking_service.entity.Booking;
import com.example.booking_service.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.userEmail = :userEmail AND b.scheduledAt = :scheduledAt")
    Optional<Booking> findByUserEmailAndScheduledAtWithAddons(@Param("userEmail") String userEmail, @Param("scheduledAt") LocalDateTime scheduledAt);

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.status = :status")
    List<Booking> findByStatusWithAddons(@Param("status") BookingStatus status);

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.scheduledAt BETWEEN :start AND :end")
    List<Booking> findByScheduledAtBetweenWithAddons(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.userEmail = :userEmail AND b.status = :status")
    List<Booking> findByUserEmailAndStatusWithAddons(@Param("userEmail") String userEmail, @Param("status") BookingStatus status);
    
    boolean existsByUserEmailAndScheduledAt(String userEmail, LocalDateTime scheduledAt);

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.id = :id")
    Optional<Booking> findByIdWithAddons(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons")
    List<Booking> findAllWithAddons();

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.userEmail = :email")
    List<Booking> findByUserEmailWithAddons(@Param("email") String email);

    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.addons WHERE b.reference = :reference")
    Optional<Booking> findByReferenceWithAddons(@Param("reference") String reference);
    
    boolean existsByReference(String reference);
}
