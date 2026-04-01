package com.cookiegram.cookiegram.orders;

public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    IN_PROGRESS("In Progress"),
    BAKED("Baked"),
    READY("Ready"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}