package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.enums.RefundStatus;
import com.sulaks.TechSpark.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepo extends JpaRepository<Refund, Long> {

    List<Refund> findAllByOrderByRefundIdDesc();

    List<Refund> findByOrder_User_UserIdOrderByRefundIdDesc(Long userId);

    Optional<Refund> findByRefundIdAndOrder_User_UserId(Long refundId, Long userId);

    List<Refund> findByOrder_OrderIdOrderByRefundIdDesc(Long orderId);

    Optional<Refund> findByReturnRequest_ReturnId(Long returnId);

    boolean existsByReturnRequest_ReturnIdAndStatus(Long returnId, RefundStatus status);
}