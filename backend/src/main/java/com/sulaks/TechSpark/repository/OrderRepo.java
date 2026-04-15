package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByUser_UserIdOrderByOrderIdDesc(Long userId);

    Optional<Order> findByOrderIdAndUser_UserId(Long orderId, Long userId);

    List<Order> findByStatusOrderByOrderIdDesc(OrderStatus status);
}