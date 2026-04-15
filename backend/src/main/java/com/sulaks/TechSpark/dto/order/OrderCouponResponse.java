package com.sulaks.TechSpark.dto.order;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderCouponResponse {

    private Long couponId;
    private String code;
    private BigDecimal discountAmount;
}