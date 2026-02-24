package com.cookiegram.cookiegram.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/post-login")
    public String postLogin(Authentication auth) {
        var roles = auth.getAuthorities().toString();

        if (roles.contains("ROLE_ADMIN")) return "redirect:/admin";
        if (roles.contains("ROLE_EMPLOYEE")) return "redirect:/employee";
        return "redirect:/customer";
    }

    @GetMapping("/customer")
    public String customer() {
        return "customer";
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}