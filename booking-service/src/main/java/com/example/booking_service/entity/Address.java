package com.example.booking_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    private String street;
    private String suburb;
    private String city;
    private String state;
    private String postalCode;
}
