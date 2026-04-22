package com.sulaks.TechSpark.dto.refund;

import com.sulaks.TechSpark.enums.RefundMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRefundRequest {

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotNull(message = "Return id is required")
    private Long returnId;

    @NotNull(message = "Refund method is required")
    private RefundMethod method;

    @NotNull(message = "Refund amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 120, message = "Reference cannot exceed 120 characters")
    private String reference;
}