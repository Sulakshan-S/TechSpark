package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.coupon.CouponRequest;
import com.sulaks.TechSpark.dto.coupon.CouponResponse;
import com.sulaks.TechSpark.dto.coupon.CouponValidationResponse;
import com.sulaks.TechSpark.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCouponById(couponId));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.getCouponByCode(code));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CouponResponse>> searchCoupons(@RequestParam String keyword) {
        return ResponseEntity.ok(couponService.searchCoupons(keyword));
    }

    @GetMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(
            @RequestParam String code,
            @RequestParam BigDecimal orderAmount
    ) {
        return ResponseEntity.ok(couponService.validateCoupon(code, orderAmount));
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponRequest request
    ) {
        return ResponseEntity.ok(couponService.updateCoupon(couponId, request));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok("Coupon deleted successfully");
    }
}