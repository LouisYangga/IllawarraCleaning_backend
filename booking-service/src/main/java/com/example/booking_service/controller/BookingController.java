package com.example.booking_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booking_service.dto.BookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.UpdateBookingDTO;
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

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody CreateBookingDTO createBookingDto) {
        try {
            BookingDTO createdBooking = bookingService.createBooking(createBookingDto);
            return ResponseEntity.ok(createdBooking);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409)
                .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getBookingsByEmail(
            @PathVariable @jakarta.validation.constraints.Email String email) {
        List<BookingDTO> bookings = bookingService.getBookingsByEmail(email);
        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable String status) {
        BookingStatus bookingStatus;
        try {
            bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "Invalid status value. Allowed values: " + java.util.Arrays.toString(BookingStatus.values())));
        }
        List<BookingDTO> bookings = bookingService.getBookingsByStatus(bookingStatus);
        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<BookingDTO> bookings = bookingService.getBookingsByDateRange(start, end);
        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody UpdateBookingDTO updateBookingDto) {
        return bookingService.updateBooking(id, updateBookingDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        BookingStatus bookingStatus;
        try {
            bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", "Invalid status value. Allowed values: " + java.util.Arrays.toString(BookingStatus.values())));
        }
        return bookingService.updateBookingStatus(id, bookingStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        BookingDTO deletedBooking = bookingService.getBookingById(id).orElse(null);
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
}
