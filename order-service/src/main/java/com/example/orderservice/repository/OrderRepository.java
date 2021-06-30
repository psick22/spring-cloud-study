package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(String orderId);

    List<Order> findByUserId(String userId);
}
