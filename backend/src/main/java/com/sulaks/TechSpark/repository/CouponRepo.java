package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepo extends JpaRepository<Coupon, Long> {

    boolean existsByCodeIgnoreCase(String code);

    Optional<Coupon> findByCodeIgnoreCase(String code);

    List<Coupon> findByCodeContainingIgnoreCase(String keyword);
}