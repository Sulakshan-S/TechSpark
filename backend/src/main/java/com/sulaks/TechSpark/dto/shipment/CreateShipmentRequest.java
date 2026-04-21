package com.sulaks.TechSpark.dto.shipment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateShipmentRequest {

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotBlank(message = "Carrier is required")
    private String carrier;

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @FutureOrPresent(message = "Estimated delivery date must be today or a future date")
    private LocalDate estimatedDeliveryDate;

    private String message;
}