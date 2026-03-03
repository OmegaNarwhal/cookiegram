package com.cookiegram.cookiegram.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee";
    }
}
