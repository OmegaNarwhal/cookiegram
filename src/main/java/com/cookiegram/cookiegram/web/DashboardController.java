package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.orders.OrderStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    private final CookieOrderService cookieOrderService;

    public DashboardController(CookieOrderService cookieOrderService) {
        this.cookieOrderService = cookieOrderService;
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/employee")
    public String employee(Model model) {
        model.addAttribute("orders", cookieOrderService.getAllOrders());
        model.addAttribute("statuses", OrderStatus.values());
        return "employee";
    }

    @PostMapping("/employee/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam OrderStatus status) {
        cookieOrderService.updateStatus(id, status);
        return "redirect:/employee";
    }
}
