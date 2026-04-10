package com.sulaks.TechSpark.dto.productimage;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductImageResponse {

    private Long productImageId;
    private Long productId;
    private String productName;
    private String url;
    private boolean isPrimary;
    private LocalDateTime createdAt;
}