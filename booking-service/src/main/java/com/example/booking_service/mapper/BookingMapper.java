package com.example.booking_service.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.example.booking_service.dto.AddonsUpdateAware;
import com.example.booking_service.dto.AdminBookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.PublicBookingDTO;
import com.example.booking_service.dto.UpdateBookingDTO;
import com.example.booking_service.dto.UserUpdateBookingDTO;
import com.example.booking_service.entity.AddOnsUpdate;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.validator.AddOnsValidator;

@Component
public class BookingMapper {
    private final AddOnsValidator addOnsValidator;

    public BookingMapper(AddOnsValidator addOnsValidator) {
        this.addOnsValidator = addOnsValidator;
    }

    public AdminBookingDTO toAdminDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        AdminBookingDTO bookingDTO = new AdminBookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setReference(booking.getReference());
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
      public PublicBookingDTO toPublicBookingDTO (Booking booking) {
        if (booking == null) {
            return null;
        }
        
        PublicBookingDTO bookingDTO = new PublicBookingDTO();
        bookingDTO.setReference(booking.getReference());
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
    public void updateEntityFromUserDTO(UserUpdateBookingDTO updateBookingDTO, Booking booking){
        if (updateBookingDTO.getFirstName() != null) booking.setFirstName(updateBookingDTO.getFirstName());
        if (updateBookingDTO.getLastName() != null) booking.setLastName(updateBookingDTO.getLastName());
        if (updateBookingDTO.getPhoneNumber() != null) booking.setPhoneNumber(updateBookingDTO.getPhoneNumber());
        if (updateBookingDTO.getNotes() != null) booking.setNotes(updateBookingDTO.getNotes());
        if (updateBookingDTO.getAddress() != null) booking.setAddress(updateBookingDTO.getAddress());
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
        handleAddonsUpdate(updateBookingDTO, booking);
    }
    private <T extends AddonsUpdateAware> void handleAddonsUpdate(T dto, Booking booking) {
        AddOnsUpdate addonsUpdate = dto.getAddons();
        if (addonsUpdate != null) {
            addOnsValidator.validateAddOnsUpdate(addonsUpdate);
            
            if (addonsUpdate.getReplace() != null) {
                booking.setAddons(new ArrayList<>(addonsUpdate.getReplace()));
            } else {
                if (booking.getAddons() == null) {
                    booking.setAddons(new ArrayList<>());
                }
                
                if (addonsUpdate.getAdd() != null) {
                    booking.getAddons().addAll(addonsUpdate.getAdd());
                }
                
                if (addonsUpdate.getRemove() != null) {
                    booking.getAddons().removeAll(addonsUpdate.getRemove());
                }
            }
        }
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
        booking.setAddons(createBookingDTO.getAddons() != null ? 
            new ArrayList<>(createBookingDTO.getAddons()) : new ArrayList<>());
        return booking;
    }
}
