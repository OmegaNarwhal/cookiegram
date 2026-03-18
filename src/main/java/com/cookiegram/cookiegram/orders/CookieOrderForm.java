package com.cookiegram.cookiegram.orders;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class CookieOrderForm {

    @NotNull(message = "Please select a cookie type.")
    private CookieType cookieType;

    @NotBlank(message = "Please enter a message.")
    @Size(max = 40, message = "Message must be 40 characters or less.")
    private String customMessage;

    private boolean sprinkles;

    @Min(value = 1, message = "Quantity must be at least 1.")
    @Max(value = 12, message = "Quantity cannot exceed 12.")
    private int quantity = 1;

    @NotBlank(message = "Recipient name is required.")
    private String recipientName;

    @NotBlank(message = "Delivery address is required.")
    private String deliveryAddress;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String customerEmail;

    public CookieType getCookieType() {
        return cookieType;
    }

    public void setCookieType(CookieType cookieType) {
        this.cookieType = cookieType;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public boolean isSprinkles() {
        return sprinkles;
    }

    public void setSprinkles(boolean sprinkles) {
        this.sprinkles = sprinkles;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCustomerEmail() {
    return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
    }
}