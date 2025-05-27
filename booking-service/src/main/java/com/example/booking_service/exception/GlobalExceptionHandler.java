package com.example.booking_service.exception;

import com.example.booking_service.entity.ServiceType;
import com.example.booking_service.entity.AddOns;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleEnumParseError(HttpMessageNotReadableException ex) {
        String message = "Invalid enum value in request. " +
            "Allowed ServiceType values: " + java.util.Arrays.toString(ServiceType.values()) +
            " | Allowed AddOns values: " + java.util.Arrays.toString(AddOns.values());
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
    }
}
