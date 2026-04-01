package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.promotions.PromotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromotionService promotionService;

    @MockitoBean
    private CookieOrderService cookieOrderService;

    @Test
    void customerLanding_shouldReturnCustomerViewWithPromotionsAndOrders() throws Exception {
        var auth = new TestingAuthenticationToken("customer@test.com", "pass", "ROLE_CUSTOMER");

        when(promotionService.getCurrentPromotions()).thenReturn(List.of());
        when(cookieOrderService.getOrdersForCustomer("customer@test.com")).thenReturn(List.of());

        mockMvc.perform(get("/customer").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("customer"))
                .andExpect(model().attributeExists("promotions"))
                .andExpect(model().attributeExists("orders"));
    }
}
