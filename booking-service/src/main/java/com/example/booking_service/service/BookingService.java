package com.example.booking_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_service.dto.AdminBookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.PublicBookingDTO;
import com.example.booking_service.dto.QuotationRequest;
import com.example.booking_service.dto.QuotationResponse;
import com.example.booking_service.dto.UpdateBookingDTO;
import com.example.booking_service.dto.UserCreationEventDTO;
import com.example.booking_service.dto.UserUpdateBookingDTO;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.entity.BookingStatus;
import com.example.booking_service.mapper.BookingMapper;
import com.example.booking_service.repository.BookingRepository;

@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserEventPublisher userEventPublisher;
    private final QuotationService quotationService;
    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper,
    UserEventPublisher userEventPublisher, QuotationService quotationService) {
        this.quotationService = quotationService;
        this.userEventPublisher = userEventPublisher;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }
    
    // Create
    @Transactional
    public PublicBookingDTO createBooking(CreateBookingDTO createBookingDTO) {
        // Check if booking already exists for this user at this time
        if (bookingRepository.existsByUserEmailAndScheduledAt(
                createBookingDTO.getUserEmail(), 
                createBookingDTO.getScheduledAt())) {
            throw new IllegalStateException("Booking already exists for this user at this time");
        }

        Booking booking = bookingMapper.toEntity(createBookingDTO);
        booking.setStatus(BookingStatus.PENDING); // Set initial status

        // If a quotation ID is provided, fetch the quotation details
        if (createBookingDTO.getQuotationId() != null) {
            QuotationResponse quotation = quotationService.getQuotation(createBookingDTO.getQuotationId());
            booking.setPrice(quotation.getPrice());
            booking.setServiceType(quotation.getServiceType());
            booking.setDuration(quotation.getDuration());
            booking.setAddons(quotation.getAddons());
        }
        
        Booking savedBooking = bookingRepository.save(booking);

        //Publish user creation event to notify user services
        UserCreationEventDTO userCreationEvent = new UserCreationEventDTO(
                booking.getFirstName(),
                booking.getLastName(),
                booking.getUserEmail(),
                booking.getPhoneNumber()
        );

        //publish Event to message broker (e.g., Kafka, RabbitMQ)
        userEventPublisher.publishUserEvent(userCreationEvent);
        return bookingMapper.toPublicBookingDTO(savedBooking);
    }
    
    // Read operations
    public List<AdminBookingDTO> getAllBookings() {
        return bookingRepository.findAllWithAddons().stream()
                .map(bookingMapper::toAdminDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<AdminBookingDTO> getBookingById(Long id) {
        return bookingRepository.findByIdWithAddons(id)
                .map(bookingMapper::toAdminDTO);
    }
    
    public List<PublicBookingDTO> getBookingsByEmail(String email) {
        return bookingRepository.findByUserEmailWithAddons(email).stream()
                .map(bookingMapper::toPublicBookingDTO)
                .collect(Collectors.toList());
    }
    
    public List<AdminBookingDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatusWithAddons(status).stream()
                .map(bookingMapper::toAdminDTO)
                .collect(Collectors.toList());
    }
    
     public Optional<?> getBookingByReference(String reference) {
        return bookingRepository.findByReferenceWithAddons(reference)
                .map(bookingMapper::toPublicBookingDTO);
    }

    public List<AdminBookingDTO> getBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByScheduledAtBetweenWithAddons(start, end).stream()
                .map(bookingMapper::toAdminDTO)
                .collect(Collectors.toList());
    }
    
    // Update
    @Transactional
    public Optional<AdminBookingDTO> updateBooking(Long id, UpdateBookingDTO updateDTO) {
        return bookingRepository.findByIdWithAddons(id)
                .map(existingBooking -> {
                    validateUpdate(existingBooking, updateDTO);
                    boolean needsPriceUpdate = isPriceUpdateRequired(existingBooking, updateDTO);
                    
                    bookingMapper.updateEntityFromDTO(updateDTO, existingBooking);
                    
                    if (needsPriceUpdate) {
                        updateBookingPricing(existingBooking);
                    }
                    
                    Booking savedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toAdminDTO(savedBooking);
                });
    }
    @Transactional
    public Optional<PublicBookingDTO> updateBookingWithReference(String reference, UserUpdateBookingDTO updateDTO) {
        return bookingRepository.findByReferenceWithAddons(reference)
                .map(existingBooking -> {
                    validateUpdate(existingBooking, updateDTO);
                    boolean needsPriceUpdate = isPriceUpdateRequired(existingBooking, updateDTO);
                    
                    bookingMapper.updateEntityFromUserDTO(updateDTO, existingBooking);
                    
                    if (needsPriceUpdate) {
                        updateBookingPricing(existingBooking);
                    }
                    
                    Booking savedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toPublicBookingDTO(savedBooking);
                });
    }
    // Delete
    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    // Status Update
    @Transactional
    public Optional<AdminBookingDTO> updateBookingStatus(Long id, BookingStatus newStatus) {
        return bookingRepository.findByIdWithAddons(id)
                .map(existingBooking -> {
                    existingBooking.setStatus(newStatus);
                    Booking savedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toAdminDTO(savedBooking);
                });
    }


    private <T> boolean isPriceUpdateRequired(Booking existingBooking, T updateDTO) {
        if (updateDTO instanceof UpdateBookingDTO) {
            UpdateBookingDTO adminUpdate = (UpdateBookingDTO) updateDTO;
            boolean serviceTypeChanged = adminUpdate.getServiceType() != null && 
                !adminUpdate.getServiceType().equals(existingBooking.getServiceType());
            boolean addonsChanged = adminUpdate.getAddons() != null && 
                !adminUpdate.getAddons().equals(existingBooking.getAddons());
            return serviceTypeChanged || addonsChanged;
        } else if (updateDTO instanceof UserUpdateBookingDTO) {
            UserUpdateBookingDTO userUpdate = (UserUpdateBookingDTO) updateDTO;
            boolean addonsChanged = userUpdate.getAddons() != null && 
                !userUpdate.getAddons().equals(existingBooking.getAddons());
            return addonsChanged;  // Users can only change addons
        }
        return false;
    }

    private <T> void validateUpdate(Booking booking, T updateDTO) {
        if (updateDTO instanceof UserUpdateBookingDTO) {
            // Validate user update restrictions
            if (booking.getScheduledAt().minusHours(24).isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Bookings cannot be modified within 24 hours of scheduled time");
            }
        }
    }

    private void updateBookingPricing(Booking booking) {
        // Create a quotation request to calculate new price
        QuotationRequest request = new QuotationRequest(
            booking.getServiceType(),
            booking.getAddons(),
            booking.getDuration()
        );
        
        QuotationResponse quotation = quotationService.createQuotation(request);
        
        // Update booking with new pricing
        booking.setPrice(quotation.getPrice());
        booking.setDuration(quotation.getDuration());
    }
}
