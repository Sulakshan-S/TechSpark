package com.sulaks.TechSpark.dto.refund;

import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.PaymentMethod;
import com.sulaks.TechSpark.enums.PaymentStatus;
import com.sulaks.TechSpark.enums.RefundMethod;
import com.sulaks.TechSpark.enums.RefundStatus;
import com.sulaks.TechSpark.enums.ReturnStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class RefundResponse {

    private Long refundId;

    private Long orderId;
    private OrderStatus orderStatus;

    private Long paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private Long returnId;
    private ReturnStatus returnStatus;

    private RefundMethod method;
    private RefundStatus status;
    private BigDecimal amount;
    private String reference;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long processedByUserId;
    private String processedByUserName;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
}