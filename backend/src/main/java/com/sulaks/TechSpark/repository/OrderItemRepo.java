package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_OrderId(Long orderId);
}