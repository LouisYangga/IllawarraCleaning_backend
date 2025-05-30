package com.example.booking_service.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.example.booking_service.dto.BookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.UpdateBookingDTO;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.validator.AddOnsValidator;

@Component
public class BookingMapper {
    private final AddOnsValidator addOnsValidator;

    public BookingMapper(AddOnsValidator addOnsValidator) {
        this.addOnsValidator = addOnsValidator;
    }

    public BookingDTO toDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setUserEmail(booking.getUserEmail());
        bookingDTO.setFirstName(booking.getFirstName());
        bookingDTO.setLastName(booking.getLastName());
        bookingDTO.setPhoneNumber(booking.getPhoneNumber());
        bookingDTO.setScheduledAt(booking.getScheduledAt());
        bookingDTO.setServiceType(booking.getServiceType());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setDuration(booking.getDuration());
        bookingDTO.setPrice(booking.getPrice());
        bookingDTO.setNotes(booking.getNotes());
        bookingDTO.setAddress(booking.getAddress());
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        bookingDTO.setUpdatedAt(booking.getUpdatedAt());
        bookingDTO.setAddons(booking.getAddons());
        return bookingDTO;
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
        // Handle empty addons
        booking.setAddons(createBookingDTO.getAddons() != null ? 
            createBookingDTO.getAddons() : new HashSet<>());
        
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
        if (updateBookingDTO.getDuration()!=null && updateBookingDTO.getDuration() > 0) {
            booking.setDuration(updateBookingDTO.getDuration());
        }
        if (updateBookingDTO.getPrice()!=null && updateBookingDTO.getPrice() > 0) {
            booking.setPrice(updateBookingDTO.getPrice());
        }
        if (updateBookingDTO.getAddress() != null) {
            booking.setAddress(updateBookingDTO.getAddress());
        }
        if (updateBookingDTO.getNotes() != null) {
            booking.setNotes(updateBookingDTO.getNotes());
        }

        // debug logging
        //     System.out.println("UpdateDTO addons: " + updateBookingDTO.getAddons());
            
        // Handle addons update with validation
        if (updateBookingDTO.getAddons() != null) {
            addOnsValidator.validateAddOnsUpdate(updateBookingDTO.getAddons());
            
            if (updateBookingDTO.getAddons().getReplace() != null) {
                booking.setAddons(new HashSet<>(updateBookingDTO.getAddons().getReplace()));
            } else {
                if (booking.getAddons() == null) {
                    booking.setAddons(new HashSet<>());
                }
                
                if (updateBookingDTO.getAddons().getAdd() != null) {
                    booking.getAddons().addAll(updateBookingDTO.getAddons().getAdd());
                }
                
                if (updateBookingDTO.getAddons().getRemove() != null) {
                    booking.getAddons().removeAll(updateBookingDTO.getAddons().getRemove());
                }
            }
        }
    }
}
