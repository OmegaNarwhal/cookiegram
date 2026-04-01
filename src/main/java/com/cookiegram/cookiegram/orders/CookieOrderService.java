package com.cookiegram.cookiegram.orders;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CookieOrderService {

    private final CookieOrderRepository cookieOrderRepository;

    public CookieOrderService(CookieOrderRepository cookieOrderRepository) {
        this.cookieOrderRepository = cookieOrderRepository;
    }

    public BigDecimal calculatePrice(CookieOrderForm form) {
        BigDecimal basePrice = switch (form.getCookieType()) {
            case CHOCOLATE_CHIP -> BigDecimal.valueOf(18.99);
            case SUGAR -> BigDecimal.valueOf(16.99);
            case DOUBLE_CHOCOLATE -> BigDecimal.valueOf(19.99);
            case OATMEAL_RAISIN -> BigDecimal.valueOf(17.99);
        };

        BigDecimal sprinkleCharge = form.isSprinkles() ? BigDecimal.valueOf(2.00) : BigDecimal.ZERO;

        return basePrice
                .add(sprinkleCharge)
                .multiply(BigDecimal.valueOf(form.getQuantity()));
    }

    public CookieOrder placeOrder(CookieOrderForm form, String username) {
        CookieOrder order = new CookieOrder();
        order.setCustomerUsername(username);
        order.setCustomerEmail(form.getCustomerEmail());
        order.setCookieType(form.getCookieType());
        order.setCustomMessage(form.getCustomMessage());
        order.setSprinkles(form.isSprinkles());
        order.setQuantity(form.getQuantity());
        order.setRecipientName(form.getRecipientName());
        order.setDeliveryAddress(form.getDeliveryAddress());
        order.setDeliveryDate(form.getDeliveryDate());
        order.setTotalPrice(calculatePrice(form));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        order.setPaymentMethod(form.getPaymentMethod());
        order.setCardholderName(form.getCardholderName());

        String cardNumber = form.getMockCardNumber() == null ? "" : form.getMockCardNumber().replaceAll("\\s+", "");
        String lastFour = cardNumber.length() >= 4
                ? cardNumber.substring(cardNumber.length() - 4)
                : cardNumber;

        order.setCardLastFour(lastFour);

        return cookieOrderRepository.save(order);
    }

    public List<CookieOrder> getOrdersForCustomer(String username) {
        return cookieOrderRepository.findByCustomerUsernameOrderByCreatedAtDesc(username);
    }

    public List<CookieOrder> getAllOrders() {
        return cookieOrderRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<CookieOrder> filterOrders(OrderStatus status, String customerUsername) {
        boolean hasStatus = status != null;
        boolean hasUsername = customerUsername != null && !customerUsername.trim().isEmpty();

        if (hasStatus && hasUsername) {
            return cookieOrderRepository
                    .findByStatusAndCustomerUsernameContainingIgnoreCaseOrderByCreatedAtDesc(
                            status, customerUsername.trim()
                    );
        }

        if (hasStatus) {
            return cookieOrderRepository.findByStatusOrderByCreatedAtDesc(status);
        }

        if (hasUsername) {
            return cookieOrderRepository
                    .findByCustomerUsernameContainingIgnoreCaseOrderByCreatedAtDesc(customerUsername.trim());
        }

        return cookieOrderRepository.findAllByOrderByCreatedAtDesc();
    }

    public CookieOrder getOrderById(Long id) {
        return cookieOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    public void updateStatus(Long id, OrderStatus status) {
        CookieOrder order = getOrderById(id);
        order.setStatus(status);
        cookieOrderRepository.save(order);
    }
}