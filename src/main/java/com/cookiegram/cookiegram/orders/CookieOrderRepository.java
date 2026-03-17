package com.cookiegram.cookiegram.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CookieOrderRepository extends JpaRepository<CookieOrder, Long> {
    List<CookieOrder> findByCustomerUsernameOrderByCreatedAtDesc(String customerUsername);
    List<CookieOrder> findAllByOrderByCreatedAtDesc();
}