package com.cookiegram.cookiegram.email;

import com.cookiegram.cookiegram.orders.CookieOrder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmationEmail(CookieOrder order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getCustomerEmail());
        message.setSubject("CookieGram Order Confirmation #" + order.getId());
        message.setText(
                "Thank you for your order!\n\n" +
                "Order ID: " + order.getId() + "\n" +
                "Cookie Type: " + order.getCookieType().getDisplayName() + "\n" +
                "Custom Message: " + order.getCustomMessage() + "\n" +
                "Quantity: " + order.getQuantity() + "\n" +
                "Total: $" + order.getTotalPrice() + "\n" +
                "Status: " + order.getStatus() + "\n\n" +
                "Recipient: " + order.getRecipientName() + "\n" +
                "Delivery Address: " + order.getDeliveryAddress() + "\n\n" +
                "Thank you for choosing CookieGram!"
        );

        mailSender.send(message);
    }
}
