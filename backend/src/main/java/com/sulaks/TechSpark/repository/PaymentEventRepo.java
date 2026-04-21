package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentEventRepo extends JpaRepository<PaymentEvent, Long> {

    List<PaymentEvent> findByPayment_PaymentIdOrderByEventTimeAsc(Long paymentId);
}