package com.cookiegram.cookiegram.orders;

public enum CookieType {
    CHOCOLATE_CHIP("Chocolate Chip"),
    SUGAR("Sugar Cookie"),
    DOUBLE_CHOCOLATE("Double Chocolate"),
    OATMEAL_RAISIN("Oatmeal Raisin");

    private final String displayName;

    CookieType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}