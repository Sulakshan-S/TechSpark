package com.sulaks.TechSpark.dto.coupon;

import com.sulaks.TechSpark.enums.DiscountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponValidationResponse {

    private String code;
    private boolean valid;
    private String message;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal orderAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
}