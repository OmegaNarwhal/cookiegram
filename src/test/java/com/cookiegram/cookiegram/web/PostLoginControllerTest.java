package com.cookiegram.cookiegram.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostLoginController.class)
class PostLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postLogin_shouldRedirectAdminToAdminDashboard() throws Exception {
        var auth = new TestingAuthenticationToken("admin@cookiegram.com", "pass", "ROLE_ADMIN");

        mockMvc.perform(get("/post-login").with(authentication(auth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    void postLogin_shouldRedirectEmployeeToEmployeeDashboard() throws Exception {
        var auth = new TestingAuthenticationToken("employee@cookiegram.com", "pass", "ROLE_EMPLOYEE");

        mockMvc.perform(get("/post-login").with(authentication(auth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee"));
    }

    @Test
    void postLogin_shouldRedirectCustomerToCustomerDashboard() throws Exception {
        var auth = new TestingAuthenticationToken("customer@test.com", "pass", "ROLE_CUSTOMER");

        mockMvc.perform(get("/post-login").with(authentication(auth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer"));
    }
}
