package com.example.booking_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String street;
    private String suburb;
    private String state;
    private String postalCode;

    @Override
    public String toString() {
        // Format: "street, suburb, state postalCode"
        // Example: "123 Crown Street, Wollongong, NSW 2500"
        return String.format("%s, %s, %s %s",
            street,
            suburb,
            state,
            postalCode
        );
    }
}
