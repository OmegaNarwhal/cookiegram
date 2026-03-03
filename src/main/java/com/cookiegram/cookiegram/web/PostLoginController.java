package com.cookiegram.cookiegram.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostLoginController {

    @GetMapping("/post-login")
    public String postLogin(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        if (isAdmin) {
            return "redirect:/admin";
        } else if (isEmployee) {
            return "redirect:/employee";
        } else {
            return "redirect:/customer";
        }
    }
}
