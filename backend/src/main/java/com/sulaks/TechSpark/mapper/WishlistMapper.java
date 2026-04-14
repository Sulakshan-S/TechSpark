package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.wishlist.WishlistResponse;
import com.sulaks.TechSpark.models.WishlistItem;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistResponse toResponse(WishlistItem wishlistItem) {
        return WishlistResponse.builder()
                .wishlistItemId(wishlistItem.getWishlistItemId())
                .userId(wishlistItem.getUser().getUserId())
                .userName(wishlistItem.getUser().getFullName())
                .productId(wishlistItem.getProduct().getProductId())
                .productName(wishlistItem.getProduct().getName())
                .productSlug(wishlistItem.getProduct().getSlug())
                .categoryId(wishlistItem.getProduct().getCategory().getCategoryId())
                .categoryName(wishlistItem.getProduct().getCategory().getName())
                .brandId(
                        wishlistItem.getProduct().getBrand() != null
                                ? wishlistItem.getProduct().getBrand().getBrandId()
                                : null
                )
                .brandName(
                        wishlistItem.getProduct().getBrand() != null
                                ? wishlistItem.getProduct().getBrand().getName()
                                : null
                )
                .createdAt(wishlistItem.getCreatedAt())
                .build();
    }
}