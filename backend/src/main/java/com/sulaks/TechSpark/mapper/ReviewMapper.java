package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.review.ReviewResponse;
import com.sulaks.TechSpark.models.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .userName(review.getUser().getFullName())
                .productId(review.getProduct().getProductId())
                .productName(review.getProduct().getName())
                .productSlug(review.getProduct().getSlug())
                .rating(review.getRating())
                .comment(review.getComment())
                .isApproved(review.isApproved())
                .createdAt(review.getCreatedAt())
                .build();
    }
}