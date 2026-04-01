package com.cookiegram.cookiegram.orders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CookieOrderRepositoryTest {

    @Autowired
    private CookieOrderRepository cookieOrderRepository;

    @Test
    void findByCustomerUsernameOrderByCreatedAtDesc_shouldReturnCustomerOrders() {
        CookieOrder order1 = new CookieOrder();
        order1.setCustomerUsername("customer");
        order1.setCustomerEmail("a@test.com");
        order1.setCookieType(CookieType.CHOCOLATE_CHIP);
        order1.setCustomMessage("Msg 1");
        order1.setSprinkles(false);
        order1.setQuantity(1);
        order1.setRecipientName("A");
        order1.setDeliveryAddress("Addr 1");
        order1.setDeliveryDate(LocalDate.of(2026, 4, 1));
        order1.setPaymentMethod("Credit Card");
        order1.setCardholderName("John Smith");
        order1.setCardLastFour("1111");
        order1.setTotalPrice(new BigDecimal("18.99"));
        order1.setStatus(OrderStatus.PENDING);
        order1.setCreatedAt(LocalDateTime.of(2026, 3, 17, 10, 0));

        CookieOrder order2 = new CookieOrder();
        order2.setCustomerUsername("customer");
        order2.setCustomerEmail("a@test.com");
        order2.setCookieType(CookieType.CHOCOLATE_CHIP);
        order2.setCustomMessage("Msg 2");
        order2.setSprinkles(true);
        order2.setQuantity(2);
        order2.setRecipientName("B");
        order2.setDeliveryAddress("Addr 2");
        order2.setDeliveryDate(LocalDate.of(2026, 4, 2));
        order2.setPaymentMethod("Debit Card");
        order2.setCardholderName("Jane Smith");
        order2.setCardLastFour("2222");
        order2.setTotalPrice(new BigDecimal("37.98"));
        order2.setStatus(OrderStatus.PENDING);
        order2.setCreatedAt(LocalDateTime.of(2026, 3, 17, 11, 0));

        CookieOrder other = new CookieOrder();
        other.setCustomerUsername("otheruser");
        other.setCustomerEmail("b@test.com");
        other.setCookieType(CookieType.CHOCOLATE_CHIP);
        other.setCustomMessage("Other");
        other.setSprinkles(false);
        other.setQuantity(1);
        other.setRecipientName("C");
        other.setDeliveryAddress("Addr 3");
        other.setDeliveryDate(LocalDate.of(2026, 4, 3));
        other.setPaymentMethod("PayPal");
        other.setCardholderName("Other User");
        other.setCardLastFour("3333");
        other.setTotalPrice(new BigDecimal("18.99"));
        other.setStatus(OrderStatus.PENDING);
        other.setCreatedAt(LocalDateTime.of(2026, 3, 17, 12, 0));

        cookieOrderRepository.save(order1);
        cookieOrderRepository.save(order2);
        cookieOrderRepository.save(other);

        List<CookieOrder> results =
                cookieOrderRepository.findByCustomerUsernameOrderByCreatedAtDesc("customer");

        assertEquals(2, results.size());
        assertEquals("customer", results.get(0).getCustomerUsername());
        assertEquals("customer", results.get(1).getCustomerUsername());
        assertEquals("Msg 2", results.get(0).getCustomMessage());
        assertEquals("Msg 1", results.get(1).getCustomMessage());
    }
}