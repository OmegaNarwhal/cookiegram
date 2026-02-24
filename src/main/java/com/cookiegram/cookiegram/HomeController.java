package com.cookiegram.cookiegram;

import com.cookiegram.cookiegram.promotions.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PromotionService promotionService;

    public HomeController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("promotions", promotionService.getCurrentPromotions());
        return "index";
    }
}