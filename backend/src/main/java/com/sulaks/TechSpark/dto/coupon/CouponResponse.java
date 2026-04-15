package com.sulaks.TechSpark.dto.coupon;

import com.sulaks.TechSpark.enums.DiscountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {

    private Long couponId;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private boolean isActive;
    private LocalDateTime createdAt;
}