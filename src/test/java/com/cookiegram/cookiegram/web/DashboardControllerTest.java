package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.orders.OrderStatus;
import com.cookiegram.cookiegram.users.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CookieOrderService cookieOrderService;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @Test
    void employee_shouldReturnEmployeeView() throws Exception {
        var auth = new TestingAuthenticationToken("employee@cookiegram.com", "pass", "ROLE_EMPLOYEE");

        when(cookieOrderService.filterOrders(null, null)).thenReturn(List.of());

        mockMvc.perform(get("/employee").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("employee"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void employee_shouldFilterByStatusAndUsername() throws Exception {
        var auth = new TestingAuthenticationToken("employee@cookiegram.com", "pass", "ROLE_EMPLOYEE");

        when(cookieOrderService.filterOrders(OrderStatus.PENDING, "customer@test.com")).thenReturn(List.of());

        mockMvc.perform(get("/employee")
                        .with(authentication(auth))
                        .param("status", "PENDING")
                        .param("customerUsername", "customer@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee"))
                .andExpect(model().attribute("selectedStatus", OrderStatus.PENDING))
                .andExpect(model().attribute("customerUsername", "customer@test.com"));

        verify(cookieOrderService).filterOrders(OrderStatus.PENDING, "customer@test.com");
    }

    @Test
    void updateOrderStatus_shouldRedirectToEmployee() throws Exception {
        var auth = new TestingAuthenticationToken("employee@cookiegram.com", "pass", "ROLE_EMPLOYEE");

        mockMvc.perform(post("/employee/orders/1/status")
                        .with(authentication(auth))
                        .with(csrf())
                        .param("status", "READY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee"));

        verify(cookieOrderService).updateStatus(1L, OrderStatus.READY);
    }

    @Test
    void admin_shouldReturnAdminViewWithStats() throws Exception {
        var auth = new TestingAuthenticationToken("admin@cookiegram.com", "pass", "ROLE_ADMIN");

        when(cookieOrderService.getAllOrders()).thenReturn(List.of());
        when(appUserRepository.count()).thenReturn(3L);
        when(appUserRepository.countByRole("ROLE_CUSTOMER")).thenReturn(1L);
        when(appUserRepository.countByRole("ROLE_EMPLOYEE")).thenReturn(1L);
        when(appUserRepository.countByRole("ROLE_ADMIN")).thenReturn(1L);

        mockMvc.perform(get("/admin").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("totalUsers"))
                .andExpect(model().attributeExists("totalCustomers"))
                .andExpect(model().attributeExists("totalEmployees"))
                .andExpect(model().attributeExists("totalAdmins"))
                .andExpect(model().attributeExists("totalOrders"))
                .andExpect(model().attributeExists("recentOrders"));
    }
}
