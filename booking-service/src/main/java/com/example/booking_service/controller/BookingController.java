package com.example.booking_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booking_service.dto.AdminBookingDTO;
import com.example.booking_service.dto.BaseBookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.PublicBookingDTO;
import com.example.booking_service.dto.UpdateBookingDTO;
import com.example.booking_service.dto.UserUpdateBookingDTO;
import com.example.booking_service.entity.BookingStatus;
import com.example.booking_service.service.BookingService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    private ResponseEntity<?> bookingsNotFoundResponse() {
        return ResponseEntity.status(404)
                .body(java.util.Collections.singletonMap("message", "No booking(s) found"));
    }
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody CreateBookingDTO createBookingDto) {
        try {
            BaseBookingDTO createdBooking = bookingService.createBooking(createBookingDto);
            return ResponseEntity.ok(createdBooking);
        } catch (IllegalStateException e) {
            return bookingsNotFoundResponse();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        List<AdminBookingDTO> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return bookingsNotFoundResponse();
        }
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getBookingsByEmail(
            @PathVariable @jakarta.validation.constraints.Email String email) {
        List<PublicBookingDTO> bookings = bookingService.getBookingsByEmail(email);
        if (bookings.isEmpty()) {
            return bookingsNotFoundResponse();
        }
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable String status) {
        BookingStatus bookingStatus;
        try {
            bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "Invalid status value. Allowed values: " + java.util.Arrays.toString(BookingStatus.values())));
        }
        List<AdminBookingDTO> bookings = bookingService.getBookingsByStatus(bookingStatus);
        if (bookings.isEmpty()) {
            return bookingsNotFoundResponse();
        }
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AdminBookingDTO> bookings = bookingService.getBookingsByDateRange(start, end);
        if (bookings.isEmpty()) {
            return bookingsNotFoundResponse();
        }
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody UpdateBookingDTO updateBookingDto) {
        return bookingService.updateBooking(id, updateBookingDto)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(bookingsNotFoundResponse());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        BookingStatus bookingStatus;
        try {
            bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "Invalid status value. Allowed values: " + java.util.Arrays.toString(BookingStatus.values())));
        }
        return bookingService.updateBookingStatus(id, bookingStatus)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(bookingsNotFoundResponse());
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        BaseBookingDTO deletedBooking = bookingService.getBookingById(id).orElse(null);
        if (deletedBooking == null) {
            return ResponseEntity.notFound().build();
        }
        bookingService.deleteBooking(id);
        String message = String.format(
            "Booking with ID %d for email %s was deleted for schedule %s.",
            id,
            deletedBooking.getUserEmail(),
            deletedBooking.getScheduledAt()
        );
        return ResponseEntity.ok(java.util.Collections.singletonMap("message", message));
    }
    //reference endpoint
    @GetMapping("/reference/{reference}")
    public ResponseEntity<?> getBookingByReference(@PathVariable String reference) {
        return bookingService.getBookingByReference(reference)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(bookingsNotFoundResponse());
    }
    @PutMapping("/reference/{reference}")
    public ResponseEntity<?> updateBookingWithReference(@PathVariable String reference, @RequestBody UserUpdateBookingDTO updateBookingDto) {
        return bookingService.updateBookingWithReference(reference, updateBookingDto)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElse(bookingsNotFoundResponse());
    }


    
}
