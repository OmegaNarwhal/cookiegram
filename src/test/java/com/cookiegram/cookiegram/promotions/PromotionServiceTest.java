package com.cookiegram.cookiegram.promotions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PromotionServiceTest {

    @Test
    void shouldReturnAtLeastOnePromotion() {
        PromotionService service = new PromotionService();
        var promos = service.getCurrentPromotions();
        assertNotNull(promos);
        assertFalse(promos.isEmpty());
    }
}