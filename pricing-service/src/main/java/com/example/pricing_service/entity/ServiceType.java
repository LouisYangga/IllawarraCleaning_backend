package com.example.pricing_service.entity;

public enum ServiceType {
    DOMESTIC_CLEAN("Domestic Cleaning"),
    END_OF_LEASE("End of Lease Cleaning"),
    OFFICE_CLEAN("Office Cleaning"),
    DEEP_CLEAN("Deep Cleaning");

    private final String displayName;

    ServiceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
