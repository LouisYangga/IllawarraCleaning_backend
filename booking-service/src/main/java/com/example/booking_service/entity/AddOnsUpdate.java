package com.example.booking_service.entity;

import java.util.Set;

import lombok.Data;

@Data
public class AddOnsUpdate {
    private Set<AddOns> add;      // addons to add
    private Set<AddOns> remove;   // addons to remove
    private Set<AddOns> replace;  // completely replace existing addons
}