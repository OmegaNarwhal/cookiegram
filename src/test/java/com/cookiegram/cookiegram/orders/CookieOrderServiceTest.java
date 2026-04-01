package com.cookiegram.cookiegram.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CookieOrderServiceTest {

    private CookieOrderRepository cookieOrderRepository;
    private CookieOrderService cookieOrderService;

    @BeforeEach
    void setUp() {
        cookieOrderRepository = mock(CookieOrderRepository.class);
        cookieOrderService = new CookieOrderService(cookieOrderRepository);
    }

    @Test
    void calculatePrice_shouldReturnCorrectPrice_withoutSprinkles() {
        CookieOrderForm form = new CookieOrderForm();
        form.setCookieType(CookieType.CHOCOLATE_CHIP);
        form.setQuantity(2);
        form.setSprinkles(false);

        BigDecimal total = cookieOrderService.calculatePrice(form);

        assertEquals(new BigDecimal("37.98"), total);
    }

    @Test
    void calculatePrice_shouldReturnCorrectPrice_withSprinkles() {
        CookieOrderForm form = new CookieOrderForm();
        form.setCookieType(CookieType.SUGAR);
        form.setQuantity(2);
        form.setSprinkles(true);

        BigDecimal total = cookieOrderService.calculatePrice(form);

        assertEquals(new BigDecimal("37.98"), total);
    }

    @Test
    void placeOrder_shouldCreateAndSaveOrder() {
        CookieOrderForm form = new CookieOrderForm();
        form.setCookieType(CookieType.DOUBLE_CHOCOLATE);
        form.setCustomMessage("Happy Birthday");
        form.setSprinkles(true);
        form.setQuantity(1);
        form.setRecipientName("Sarah");
        form.setDeliveryAddress("123 Main St");
        form.setCustomerEmail("test@test.com");

        when(cookieOrderRepository.save(any(CookieOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CookieOrder result = cookieOrderService.placeOrder(form, "customer");

        ArgumentCaptor<CookieOrder> captor = ArgumentCaptor.forClass(CookieOrder.class);
        verify(cookieOrderRepository).save(captor.capture());

        CookieOrder savedOrder = captor.getValue();

        assertEquals("customer", savedOrder.getCustomerUsername());
        assertEquals("test@test.com", savedOrder.getCustomerEmail());
        assertEquals(CookieType.DOUBLE_CHOCOLATE, savedOrder.getCookieType());
        assertEquals("Happy Birthday", savedOrder.getCustomMessage());
        assertTrue(savedOrder.isSprinkles());
        assertEquals(1, savedOrder.getQuantity());
        assertEquals("Sarah", savedOrder.getRecipientName());
        assertEquals("123 Main St", savedOrder.getDeliveryAddress());
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        assertNotNull(savedOrder.getCreatedAt());

        assertEquals(savedOrder.getCustomerUsername(), result.getCustomerUsername());
    }

    @Test
    void updateStatus_shouldUpdateOrderStatus() {
        CookieOrder order = new CookieOrder();
        order.setStatus(OrderStatus.PENDING);

        when(cookieOrderRepository.findById(1L)).thenReturn(Optional.of(order));

        cookieOrderService.updateStatus(1L, OrderStatus.READY);

        assertEquals(OrderStatus.READY, order.getStatus());
        verify(cookieOrderRepository).save(order);
    }

    @Test
    void getOrderById_shouldThrowWhenOrderMissing() {
        when(cookieOrderRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cookieOrderService.getOrderById(99L)
        );

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
void filterOrders_shouldReturnStatusFilteredOrders() {
    when(cookieOrderRepository.findByStatusOrderByCreatedAtDesc(OrderStatus.PENDING))
            .thenReturn(java.util.List.of());

    var results = cookieOrderService.filterOrders(OrderStatus.PENDING, null);

    assertNotNull(results);
    verify(cookieOrderRepository).findByStatusOrderByCreatedAtDesc(OrderStatus.PENDING);
}

@Test
void filterOrders_shouldReturnUsernameFilteredOrders() {
    when(cookieOrderRepository.findByCustomerUsernameContainingIgnoreCaseOrderByCreatedAtDesc("customer"))
            .thenReturn(java.util.List.of());

    var results = cookieOrderService.filterOrders(null, "customer");

    assertNotNull(results);
    verify(cookieOrderRepository)
            .findByCustomerUsernameContainingIgnoreCaseOrderByCreatedAtDesc("customer");
}

@Test
void filterOrders_shouldReturnStatusAndUsernameFilteredOrders() {
    when(cookieOrderRepository.findByStatusAndCustomerUsernameContainingIgnoreCaseOrderByCreatedAtDesc(
            OrderStatus.READY, "customer"))
            .thenReturn(java.util.List.of());

    var results = cookieOrderService.filterOrders(OrderStatus.READY, "customer");

    assertNotNull(results);
    verify(cookieOrderRepository)
            .findByStatusAndCustomerUsernameContainingIgnoreCaseOrderByCreatedAtDesc(OrderStatus.READY, "customer");
}

@Test
void filterOrders_shouldReturnAllOrdersWhenNoFilters() {
    when(cookieOrderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(java.util.List.of());

    var results = cookieOrderService.filterOrders(null, null);

    assertNotNull(results);
    verify(cookieOrderRepository).findAllByOrderByCreatedAtDesc();
}
}