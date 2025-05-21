package com.example.booking_service.mapper;

import org.springframework.stereotype.Component;

import com.example.booking_service.dto.BookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.UpdateBookingDTO;
import com.example.booking_service.entity.Booking;

@Component
public class BookingMapper {
    
    public BookingDTO toDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        return new BookingDTO(
            booking.getId(),
            booking.getUserEmail(),
            booking.getFirstName(),
            booking.getLastName(),
            booking.getPhoneNumber(),
            booking.getScheduledAt(),
            booking.getServiceType(),
            booking.getStatus(),
            booking.getDuration(),
            booking.getPrice(),
            booking.getNotes(),
            booking.getAddress(),
            booking.getCreatedAt(),
            booking.getUpdatedAt()
        );
    }
    
    public Booking toEntity(CreateBookingDTO createBookingDTO) {
        if (createBookingDTO == null) {
            return null;
        }
        
        Booking booking = new Booking();
        booking.setUserEmail(createBookingDTO.getUserEmail());
        booking.setFirstName(createBookingDTO.getFirstName());
        booking.setLastName(createBookingDTO.getLastName());
        booking.setPhoneNumber(createBookingDTO.getPhoneNumber());
        booking.setScheduledAt(createBookingDTO.getScheduledAt());
        booking.setServiceType(createBookingDTO.getServiceType());
        booking.setDuration(createBookingDTO.getDuration());
        booking.setPrice(createBookingDTO.getPrice());
        booking.setAddress(createBookingDTO.getAddress());
        booking.setNotes(createBookingDTO.getNotes());
        
        return booking;
    }
    
    public void updateEntityFromDTO(UpdateBookingDTO updateBookingDTO, Booking booking) {
        if (updateBookingDTO == null || booking == null) {
            return;
        }
        
        if (updateBookingDTO.getFirstName() != null) {
            booking.setFirstName(updateBookingDTO.getFirstName());
        }
        if (updateBookingDTO.getLastName() != null) {
            booking.setLastName(updateBookingDTO.getLastName());
        }
        if (updateBookingDTO.getPhoneNumber() != null) {
            booking.setPhoneNumber(updateBookingDTO.getPhoneNumber());
        }
        if (updateBookingDTO.getScheduledAt() != null) {
            booking.setScheduledAt(updateBookingDTO.getScheduledAt());
        }
        if (updateBookingDTO.getServiceType() != null) {
            booking.setServiceType(updateBookingDTO.getServiceType());
        }
        if (updateBookingDTO.getStatus() != null) {
            booking.setStatus(updateBookingDTO.getStatus());
        }
        if (updateBookingDTO.getDuration() > 0) {
            booking.setDuration(updateBookingDTO.getDuration());
        }
        if (updateBookingDTO.getPrice() > 0) {
            booking.setPrice(updateBookingDTO.getPrice());
        }
        if (updateBookingDTO.getAddress() != null) {
            booking.setAddress(updateBookingDTO.getAddress());
        }
        if (updateBookingDTO.getNotes() != null) {
            booking.setNotes(updateBookingDTO.getNotes());
        }
    }
}
