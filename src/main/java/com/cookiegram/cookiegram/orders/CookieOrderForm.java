package com.cookiegram.cookiegram.orders;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

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

    @NotNull(message = "Delivery date is required.")
    private LocalDate deliveryDate;

    @NotBlank(message = "Please select a payment method.")
    private String paymentMethod;

    @NotBlank(message = "Cardholder name is required.")
    private String cardholderName;

    @NotBlank(message = "Card number is required.")
    private String mockCardNumber;

    @NotBlank(message = "Expiry date is required.")
    private String expiryDate;

    @NotBlank(message = "CVV is required.")
    private String cvv;

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

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getMockCardNumber() {
        return mockCardNumber;
    }

    public void setMockCardNumber(String mockCardNumber) {
        this.mockCardNumber = mockCardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}