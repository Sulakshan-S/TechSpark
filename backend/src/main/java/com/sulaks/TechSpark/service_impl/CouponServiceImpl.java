package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.coupon.CouponRequest;
import com.sulaks.TechSpark.dto.coupon.CouponResponse;
import com.sulaks.TechSpark.dto.coupon.CouponValidationResponse;
import com.sulaks.TechSpark.enums.DiscountType;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.models.Coupon;
import com.sulaks.TechSpark.repository.CouponRepo;
import com.sulaks.TechSpark.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepo couponRepo;

    @Override
    public CouponResponse createCoupon(CouponRequest request) {
        String code = request.getCode().trim().toUpperCase();

        if (couponRepo.existsByCodeIgnoreCase(code)) {
            throw new ResourceAlreadyExistsException("Coupon code already exists");
        }

        validateCouponDates(request.getStartsAt(), request.getEndsAt());
        validateDiscount(request.getDiscountType(), request.getDiscountValue());

        Coupon coupon = Coupon.builder()
                .code(code)
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minOrderValue(request.getMinOrderValue())
                .startsAt(request.getStartsAt())
                .endsAt(request.getEndsAt())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Coupon savedCoupon = couponRepo.save(coupon);
        return mapToResponse(savedCoupon);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CouponResponse getCouponById(Long couponId) {
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + couponId));

        return mapToResponse(coupon);
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        Coupon coupon = couponRepo.findByCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));

        return mapToResponse(coupon);
    }

    @Override
    public List<CouponResponse> searchCoupons(String keyword) {
        return couponRepo.findByCodeContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CouponResponse updateCoupon(Long couponId, CouponRequest request) {
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + couponId));

        String newCode = request.getCode().trim().toUpperCase();

        if (!coupon.getCode().equalsIgnoreCase(newCode) && couponRepo.existsByCodeIgnoreCase(newCode)) {
            throw new ResourceAlreadyExistsException("Coupon code already exists");
        }

        validateCouponDates(request.getStartsAt(), request.getEndsAt());
        validateDiscount(request.getDiscountType(), request.getDiscountValue());

        coupon.setCode(newCode);
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinOrderValue(request.getMinOrderValue());
        coupon.setStartsAt(request.getStartsAt());
        coupon.setEndsAt(request.getEndsAt());
        coupon.setActive(request.getIsActive() != null ? request.getIsActive() : coupon.isActive());

        Coupon updatedCoupon = couponRepo.save(coupon);
        return mapToResponse(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + couponId));

        couponRepo.delete(coupon);
    }

    @Override
    public CouponValidationResponse validateCoupon(String code, BigDecimal orderAmount) {
        Coupon coupon = couponRepo.findByCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));

        LocalDateTime now = LocalDateTime.now();

        if (!coupon.isActive()) {
            return invalidResponse(coupon.getCode(), orderAmount, "Coupon is inactive");
        }

        if (coupon.getStartsAt() != null && now.isBefore(coupon.getStartsAt())) {
            return invalidResponse(coupon.getCode(), orderAmount, "Coupon is not active yet");
        }

        if (coupon.getEndsAt() != null && now.isAfter(coupon.getEndsAt())) {
            return invalidResponse(coupon.getCode(), orderAmount, "Coupon has expired");
        }

        if (orderAmount.compareTo(coupon.getMinOrderValue()) < 0) {
            return invalidResponse(
                    coupon.getCode(),
                    orderAmount,
                    "Minimum order value required is " + coupon.getMinOrderValue()
            );
        }

        BigDecimal discountAmount;

        if (coupon.getDiscountType() == DiscountType.PERCENT) {
            discountAmount = orderAmount
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = coupon.getDiscountValue();
        }

        if (discountAmount.compareTo(orderAmount) > 0) {
            discountAmount = orderAmount;
        }

        BigDecimal finalAmount = orderAmount.subtract(discountAmount);

        return CouponValidationResponse.builder()
                .code(coupon.getCode())
                .valid(true)
                .message("Coupon is valid")
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .orderAmount(orderAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .build();
    }

    private void validateCouponDates(LocalDateTime startsAt, LocalDateTime endsAt) {
        if (startsAt != null && endsAt != null && endsAt.isBefore(startsAt)) {
            throw new IllegalArgumentException("Coupon end date must be after start date");
        }
    }

    private void validateDiscount(DiscountType discountType, BigDecimal discountValue) {
        if (discountType == DiscountType.PERCENT
                && discountValue.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot be greater than 100");
        }
    }

    private CouponValidationResponse invalidResponse(
            String code,
            BigDecimal orderAmount,
            String message
    ) {
        return CouponValidationResponse.builder()
                .code(code)
                .valid(false)
                .message(message)
                .orderAmount(orderAmount)
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(orderAmount)
                .build();
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        return CouponResponse.builder()
                .couponId(coupon.getCouponId())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderValue(coupon.getMinOrderValue())
                .startsAt(coupon.getStartsAt())
                .endsAt(coupon.getEndsAt())
                .isActive(coupon.isActive())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}