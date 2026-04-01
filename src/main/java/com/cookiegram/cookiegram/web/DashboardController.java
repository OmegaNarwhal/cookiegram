package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.orders.CookieOrder;
import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.orders.OrderStatus;
import com.cookiegram.cookiegram.users.AppUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    private final CookieOrderService cookieOrderService;
    private final AppUserRepository appUserRepository;

    public DashboardController(CookieOrderService cookieOrderService,
                               AppUserRepository appUserRepository) {
        this.cookieOrderService = cookieOrderService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<CookieOrder> allOrders = cookieOrderService.getAllOrders();
        List<CookieOrder> recentOrders = allOrders.size() > 5 ? allOrders.subList(0, 5) : allOrders;

        model.addAttribute("totalUsers", appUserRepository.count());
        model.addAttribute("totalCustomers", appUserRepository.countByRole("ROLE_CUSTOMER"));
        model.addAttribute("totalEmployees", appUserRepository.countByRole("ROLE_EMPLOYEE"));
        model.addAttribute("totalAdmins", appUserRepository.countByRole("ROLE_ADMIN"));

        model.addAttribute("totalOrders", allOrders.size());
        model.addAttribute("pendingOrders",
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count());
        model.addAttribute("inProgressOrders",
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.IN_PROGRESS).count());
        model.addAttribute("readyOrders",
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.READY).count());
        model.addAttribute("completedOrders",
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).count());

        model.addAttribute("recentOrders", recentOrders);

        return "admin";
    }

    @GetMapping("/employee")
    public String employee(@RequestParam(required = false) OrderStatus status,
                           @RequestParam(required = false) String customerUsername,
                           Model model) {

        model.addAttribute("orders", cookieOrderService.filterOrders(status, customerUsername));
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("customerUsername", customerUsername);

        return "employee";
    }

    @PostMapping("/employee/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam OrderStatus status) {
        cookieOrderService.updateStatus(id, status);
        return "redirect:/employee";
    }
}