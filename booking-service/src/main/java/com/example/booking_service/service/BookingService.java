package com.example.booking_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_service.dto.BookingDTO;
import com.example.booking_service.dto.CreateBookingDTO;
import com.example.booking_service.dto.QuotationResponse;
import com.example.booking_service.dto.UpdateBookingDTO;
import com.example.booking_service.dto.UserCreationEventDTO;
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
    public BookingDTO createBooking(CreateBookingDTO createBookingDTO) {
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
        return bookingMapper.toDTO(savedBooking);
    }
    
    // Read operations
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAllWithAddons().stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<BookingDTO> getBookingById(Long id) {
        return bookingRepository.findByIdWithAddons(id)
                .map(bookingMapper::toDTO);
    }
    
    public List<BookingDTO> getBookingsByEmail(String email) {
        return bookingRepository.findByUserEmailWithAddons(email).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookingDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatusWithAddons(status).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }
    
     public Optional<?> getBookingByReference(String reference) {
        return bookingRepository.findByReferenceWithAddons(reference)
                .map(bookingMapper::toDTO);
    }

    public List<BookingDTO> getBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByScheduledAtBetweenWithAddons(start, end).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Update
    @Transactional
    public Optional<BookingDTO> updateBooking(Long id, UpdateBookingDTO updateBookingDTO) {
        return bookingRepository.findByIdWithAddons(id)
                .map(existingBooking -> {
                    bookingMapper.updateEntityFromDTO(updateBookingDTO, existingBooking);
                    Booking savedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toDTO(savedBooking);
                });
    }
    
    // Delete
    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    // Status Update
    @Transactional
    public Optional<BookingDTO> updateBookingStatus(Long id, BookingStatus newStatus) {
        return bookingRepository.findByIdWithAddons(id)
                .map(existingBooking -> {
                    existingBooking.setStatus(newStatus);
                    Booking savedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toDTO(savedBooking);
                });
    }
}
