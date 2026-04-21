package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findByProduct_ProductIdAndIsApprovedTrueOrderByReviewIdDesc(Long productId);

    List<Review> findByUser_UserIdOrderByReviewIdDesc(Long userId);

    List<Review> findByIsApprovedFalseOrderByReviewIdDesc();

    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
}