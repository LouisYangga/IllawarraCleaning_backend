package com.example.pricing_service.entity;

public enum AddOns {
    WINDOW_CLEANING("Window Cleaning"),
    FRIDGE_CLEANING("Fridge Cleaning"),
    OVEN_CLEANING("Oven Cleaning"),
    CARPET_CLEANING("Carpet Cleaning"),
    WALLS_CLEANING("Walls Cleaning"),
    GARAGE_CLEANING("Garage Cleaning"),
    BALCONY_CLEANING("Balcony Cleaning"),
    BEDROOM_CLEANING("Bedroom Cleaning"),
    BATHROOM_CLEANING("Bathroom Cleaning"),
    KITCHEN_CLEANING("Kitchen Cleaning"),
    BLIND_CLEANING("Blind Cleaning");

    private final String displayName;

    AddOns(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
