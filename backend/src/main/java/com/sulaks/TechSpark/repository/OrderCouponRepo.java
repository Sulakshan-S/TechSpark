package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.OrderCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderCouponRepo extends JpaRepository<OrderCoupon, Long> {

    List<OrderCoupon> findByOrder_OrderId(Long orderId);
}