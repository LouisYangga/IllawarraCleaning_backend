package com.example.booking_service.entity;

public enum ServiceType {
    DOMESTIC_CLEAN("Domestic Cleaning", 55.00),
    END_OF_LEASE("End of Lease Cleaning", 50.00),
    OFFICE_CLEAN("Office Cleaning", 60.00),
    DEEP_CLEAN("Deep Cleaning", 55.00);

    private final String displayName;
    private double basePrice;

    ServiceType(String displayName, double basePrice) {
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
