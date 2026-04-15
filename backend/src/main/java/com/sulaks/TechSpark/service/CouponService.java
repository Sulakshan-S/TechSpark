package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.coupon.CouponRequest;
import com.sulaks.TechSpark.dto.coupon.CouponResponse;
import com.sulaks.TechSpark.dto.coupon.CouponValidationResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    List<CouponResponse> getAllCoupons();

    CouponResponse getCouponById(Long couponId);

    CouponResponse getCouponByCode(String code);

    List<CouponResponse> searchCoupons(String keyword);

    CouponResponse updateCoupon(Long couponId, CouponRequest request);

    void deleteCoupon(Long couponId);

    CouponValidationResponse validateCoupon(String code, BigDecimal orderAmount);
}