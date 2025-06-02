package com.example.booking_service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminBookingDTO extends BaseBookingDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
