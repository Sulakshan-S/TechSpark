package com.sulaks.TechSpark.dto.payment;

import com.sulaks.TechSpark.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusRequest {

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    private String transactionRef;
    private String message;
}