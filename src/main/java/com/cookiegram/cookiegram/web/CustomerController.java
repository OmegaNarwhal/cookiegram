package com.cookiegram.cookiegram.web;

import com.cookiegram.cookiegram.promotions.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    private final PromotionService promotionService;

    public CustomerController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/customer")
    public String customerLanding(Model model) {
        model.addAttribute("promotions", promotionService.getCurrentPromotions());
        return "customer";
    }
}
