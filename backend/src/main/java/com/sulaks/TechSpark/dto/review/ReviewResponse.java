package com.sulaks.TechSpark.dto.review;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;

    private Long userId;
    private String userName;

    private Long productId;
    private String productName;
    private String productSlug;

    private Integer rating;
    private String comment;
    private boolean isApproved;

    private LocalDateTime createdAt;
}