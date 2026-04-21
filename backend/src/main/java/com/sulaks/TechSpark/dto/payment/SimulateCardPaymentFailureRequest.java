package com.sulaks.TechSpark.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulateCardPaymentFailureRequest {

    @NotBlank(message = "Failure message is required")
    private String message;
}