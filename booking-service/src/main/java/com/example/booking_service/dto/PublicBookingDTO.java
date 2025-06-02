package com.example.booking_service.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicBookingDTO extends BaseBookingDTO {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
