package com.example.booking_service.entity;

import java.util.List;

import lombok.Data;

@Data
public class AddOnsUpdate {
    private List<AddOns> add;      // addons to add
    private List<AddOns> remove;   // addons to remove
    private List<AddOns> replace;  // completely replace existing addons
}