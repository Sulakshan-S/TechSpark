package com.sulaks.TechSpark.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulateCardPaymentRequest {

    @NotBlank(message = "Transaction reference is required")
    private String transactionRef;
}