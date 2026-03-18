package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.email.EmailService;
import com.cookiegram.cookiegram.orders.CookieOrder;
import com.cookiegram.cookiegram.orders.CookieOrderForm;
import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.orders.CookieType;
import com.cookiegram.cookiegram.orders.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CustomerOrderController.class)
class CustomerOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CookieOrderService cookieOrderService;

    @MockitoBean
    private EmailService emailService;

    @Test
    void showOrderForm_shouldReturnOrderFormView() throws Exception {
        Authentication auth = new TestingAuthenticationToken("customer", "pass", "ROLE_CUSTOMER");

        mockMvc.perform(get("/customer/orders/new").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("order-form"))
                .andExpect(model().attributeExists("orderForm"))
                .andExpect(model().attributeExists("cookieTypes"));
    }

    @Test
    void previewOrder_shouldReturnPreviewView_whenValid() throws Exception {
        Authentication auth = new TestingAuthenticationToken("customer", "pass", "ROLE_CUSTOMER");

        when(cookieOrderService.calculatePrice(any(CookieOrderForm.class)))
                .thenReturn(new BigDecimal("18.99"));

        mockMvc.perform(post("/customer/orders/preview")
                        .with(authentication(auth))
                        .with(csrf())
                        .param("cookieType", "CHOCOLATE_CHIP")
                        .param("customMessage", "Happy Birthday")
                        .param("sprinkles", "false")
                        .param("quantity", "1")
                        .param("recipientName", "Sarah")
                        .param("deliveryAddress", "123 Main St")
                        .param("customerEmail", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-preview"))
                .andExpect(model().attributeExists("orderForm"))
                .andExpect(model().attributeExists("totalPrice"));
    }

    @Test
    void previewOrder_shouldReturnFormAgain_whenInvalid() throws Exception {
        Authentication auth = new TestingAuthenticationToken("customer", "pass", "ROLE_CUSTOMER");

        mockMvc.perform(post("/customer/orders/preview")
                        .with(authentication(auth))
                        .with(csrf())
                        .param("cookieType", "CHOCOLATE_CHIP")
                        .param("customMessage", "")
                        .param("quantity", "0")
                        .param("recipientName", "")
                        .param("deliveryAddress", "")
                        .param("customerEmail", "not-an-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-form"))
                .andExpect(model().attributeExists("cookieTypes"));
    }

    @Test
    void placeOrder_shouldSendEmailAndReturnConfirmation() throws Exception {
        Authentication auth = new TestingAuthenticationToken("customer", "pass", "ROLE_CUSTOMER");

        CookieOrder savedOrder = new CookieOrder();
        savedOrder.setCustomerUsername("customer");
        savedOrder.setCustomerEmail("test@test.com");
        savedOrder.setCookieType(CookieType.CHOCOLATE_CHIP);
        savedOrder.setCustomMessage("Happy Birthday");
        savedOrder.setSprinkles(false);
        savedOrder.setQuantity(1);
        savedOrder.setRecipientName("Sarah");
        savedOrder.setDeliveryAddress("123 Main St");
        savedOrder.setTotalPrice(new BigDecimal("18.99"));
        savedOrder.setStatus(OrderStatus.PENDING);

        when(cookieOrderService.placeOrder(any(CookieOrderForm.class), eq("customer")))
                .thenReturn(savedOrder);

        mockMvc.perform(post("/customer/orders/place")
                        .with(authentication(auth))
                        .with(csrf())
                        .param("cookieType", "CHOCOLATE_CHIP")
                        .param("customMessage", "Happy Birthday")
                        .param("sprinkles", "false")
                        .param("quantity", "1")
                        .param("recipientName", "Sarah")
                        .param("deliveryAddress", "123 Main St")
                        .param("customerEmail", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-confirmation"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("emailSent", true));

        verify(emailService).sendOrderConfirmationEmail(savedOrder);
    }

    @Test
    void myOrders_shouldReturnCustomerOrdersView() throws Exception {
        Authentication auth = new TestingAuthenticationToken("customer", "pass", "ROLE_CUSTOMER");

        when(cookieOrderService.getOrdersForCustomer("customer")).thenReturn(List.of());

        mockMvc.perform(get("/customer/orders").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("customer-orders"))
                .andExpect(model().attributeExists("orders"));
    }
}