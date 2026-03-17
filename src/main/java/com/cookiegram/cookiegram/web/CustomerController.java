package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.orders.CookieOrderService;
import com.cookiegram.cookiegram.promotions.PromotionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    private final PromotionService promotionService;
    private final CookieOrderService cookieOrderService;

    public CustomerController(PromotionService promotionService,
                              CookieOrderService cookieOrderService) {
        this.promotionService = promotionService;
        this.cookieOrderService = cookieOrderService;
    }

    @GetMapping("/customer")
    public String customerLanding(Authentication authentication, Model model) {
        model.addAttribute("promotions", promotionService.getCurrentPromotions());
        model.addAttribute("orders", cookieOrderService.getOrdersForCustomer(authentication.getName()));
        return "customer";
    }
}
