package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.payment.PaymentEventResponse;
import com.sulaks.TechSpark.dto.payment.PaymentResponse;
import com.sulaks.TechSpark.models.Payment;
import com.sulaks.TechSpark.models.PaymentEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {

    public PaymentEventResponse toPaymentEventResponse(PaymentEvent event) {
        return PaymentEventResponse.builder()
                .paymentEventId(event.getPaymentEventId())
                .eventType(event.getEventType())
                .message(event.getMessage())
                .eventTime(event.getEventTime())
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment, List<PaymentEvent> events) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .userId(payment.getOrder().getUser().getUserId())
                .userName(payment.getOrder().getUser().getFullName())
                .userEmail(payment.getOrder().getUser().getEmail())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .transactionRef(payment.getTransactionRef())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .events(events.stream().map(this::toPaymentEventResponse).toList())
                .build();
    }
}