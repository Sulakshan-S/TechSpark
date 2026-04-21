package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_OrderId(Long orderId);

    Optional<Payment> findByPaymentIdAndOrder_User_UserId(Long paymentId, Long userId);

    Optional<Payment> findByOrder_OrderIdAndOrder_User_UserId(Long orderId, Long userId);

    List<Payment> findByOrder_User_UserIdOrderByPaymentIdDesc(Long userId);

    boolean existsByOrder_OrderId(Long orderId);
}