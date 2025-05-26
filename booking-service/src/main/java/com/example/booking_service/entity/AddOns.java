package com.example.booking_service.entity;

public enum AddOns {
    WINDOW_CLEANING("Window Cleaning", 30.00),
    FRIDGE_CLEANING("Fridge Cleaning", 35.00),
    OVEN_CLEANING("Oven Cleaning", 50.00),
    CARPET_CLEANING("Carpet Cleaning", 65.00),
    WALLS_CLEANING("Walls Cleaning", 40.00),
    GARAGE_CLEANING("Garage Cleaning", 50.00),
    BALCONY_CLEANING("Balcony Cleaning", 35.00),
    BLIND_CLEANING("Blind Cleaning", 20.00);

    private final String displayName;
    private double basePrice;

    AddOns(String displayName, double basePrice) {
        this.displayName = displayName;
        this.basePrice = basePrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
