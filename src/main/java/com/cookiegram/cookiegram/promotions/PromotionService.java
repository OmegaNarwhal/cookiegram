package com.cookiegram.cookiegram.promotions;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PromotionService {

    public List<Promotion> getCurrentPromotions() {
        return List.of(
                new Promotion("15% Off", "Save 15% on message cookies this week!"),
                new Promotion("Buy 1 Get 1 Free", "BOGO on classic chocolate chip (limited time).")
        );
    }
}


