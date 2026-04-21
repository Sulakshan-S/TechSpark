package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.review.ReviewRequest;
import com.sulaks.TechSpark.dto.review.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(ReviewRequest request, String userEmail);

    List<ReviewResponse> getApprovedReviewsByProduct(Long productId);

    List<ReviewResponse> getMyReviews(String userEmail);

    List<ReviewResponse> getAllReviews();

    List<ReviewResponse> getPendingReviews();

    ReviewResponse approveReview(Long reviewId);

    void deleteReview(Long reviewId);
}