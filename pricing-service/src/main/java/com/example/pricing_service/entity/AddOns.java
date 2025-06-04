package com.example.pricing_service.entity;

public enum AddOns {
    WINDOW_CLEANING("Window Cleaning"),
    CARPET_CLEANING("Carpet Cleaning"),
    UPHOLSTERY_CLEANING("Upholstery Cleaning"),
    BATHROOM_DEEP_CLEAN("Bathroom Deep Clean"),
    KITCHEN_DEEP_CLEAN("Kitchen Deep Clean"),
    FRIDGE_CLEANING("Fridge Cleaning"),
    OVEN_CLEANING("Oven Cleaning");

    private final String displayName;

    AddOns(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
