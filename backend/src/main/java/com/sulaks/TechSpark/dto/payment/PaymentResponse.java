package com.sulaks.TechSpark.dto.payment;

import com.sulaks.TechSpark.enums.PaymentMethod;
import com.sulaks.TechSpark.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PaymentResponse {

    private Long paymentId;

    private Long orderId;
    private Long userId;
    private String userName;
    private String userEmail;

    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private String transactionRef;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<PaymentEventResponse> events;
}