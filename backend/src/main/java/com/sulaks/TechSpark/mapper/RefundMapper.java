package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.refund.RefundResponse;
import com.sulaks.TechSpark.models.Refund;
import org.springframework.stereotype.Component;

@Component
public class RefundMapper {

    public RefundResponse toRefundResponse(Refund refund) {
        return RefundResponse.builder()
                .refundId(refund.getRefundId())

                .orderId(refund.getOrder().getOrderId())
                .orderStatus(refund.getOrder().getStatus())

                .paymentId(refund.getPayment() != null ? refund.getPayment().getPaymentId() : null)
                .paymentMethod(refund.getPayment() != null ? refund.getPayment().getMethod() : null)
                .paymentStatus(refund.getPayment() != null ? refund.getPayment().getStatus() : null)

                .returnId(refund.getReturnRequest() != null ? refund.getReturnRequest().getReturnId() : null)
                .returnStatus(refund.getReturnRequest() != null ? refund.getReturnRequest().getStatus() : null)

                .method(refund.getMethod())
                .status(refund.getStatus())
                .amount(refund.getAmount())
                .reference(refund.getReference())

                .userId(refund.getOrder().getUser().getUserId())
                .userName(refund.getOrder().getUser().getFullName())
                .userEmail(refund.getOrder().getUser().getEmail())

                .processedByUserId(
                        refund.getProcessedByUser() != null ? refund.getProcessedByUser().getUserId() : null
                )
                .processedByUserName(
                        refund.getProcessedByUser() != null ? refund.getProcessedByUser().getFullName() : null
                )
                .processedAt(refund.getProcessedAt())
                .createdAt(refund.getCreatedAt())
                .build();
    }
}