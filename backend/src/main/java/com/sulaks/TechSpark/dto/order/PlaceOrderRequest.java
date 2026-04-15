package com.sulaks.TechSpark.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlaceOrderRequest {

    @NotNull(message = "Shipping address id is required")
    private Long shippingAddressId;

    private String couponCode;

    @DecimalMin(value = "0.0", inclusive = true, message = "Shipping fee must be zero or greater")
    private BigDecimal shippingFee = BigDecimal.ZERO;
}