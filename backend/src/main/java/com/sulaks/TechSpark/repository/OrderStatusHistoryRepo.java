package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrder_OrderIdOrderByChangedAtAsc(Long orderId);
}