package com.sulaks.TechSpark.dto.wishlist;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WishlistResponse {

    private Long wishlistItemId;

    private Long userId;
    private String userName;

    private Long productId;
    private String productName;
    private String productSlug;

    private Long categoryId;
    private String categoryName;

    private Long brandId;
    private String brandName;

    private LocalDateTime createdAt;
}