package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.orders.CookieOrder;
import com.cookiegram.cookiegram.orders.CookieOrderForm;
import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.orders.CookieType;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    private final CookieOrderService cookieOrderService;

    public CustomerOrderController(CookieOrderService cookieOrderService) {
        this.cookieOrderService = cookieOrderService;
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        if (!model.containsAttribute("orderForm")) {
            model.addAttribute("orderForm", new CookieOrderForm());
        }
        model.addAttribute("cookieTypes", CookieType.values());
        return "order-form";
    }

    @PostMapping("/preview")
    public String previewOrder(@Valid @ModelAttribute("orderForm") CookieOrderForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cookieTypes", CookieType.values());
            return "order-form";
        }

        model.addAttribute("orderForm", form);
        model.addAttribute("totalPrice", cookieOrderService.calculatePrice(form));
        return "order-preview";
    }

    @PostMapping("/place")
    public String placeOrder(@ModelAttribute("orderForm") CookieOrderForm form,
                             Authentication authentication,
                             Model model) {
        CookieOrder placedOrder = cookieOrderService.placeOrder(form, authentication.getName());
        model.addAttribute("order", placedOrder);
        return "order-confirmation";
    }

    @GetMapping
    public String myOrders(Authentication authentication, Model model) {
        model.addAttribute("orders", cookieOrderService.getOrdersForCustomer(authentication.getName()));
        return "customer-orders";
    }
}
