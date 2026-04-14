package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.wishlist.WishlistRequest;
import com.sulaks.TechSpark.dto.wishlist.WishlistResponse;

import java.util.List;

public interface WishlistService {

    WishlistResponse addToWishlist(WishlistRequest request, String userEmail);

    List<WishlistResponse> getMyWishlist(String userEmail);

    void removeWishlistItem(Long wishlistItemId, String userEmail);

    void removeFromWishlistByProduct(Long productId, String userEmail);

    boolean isProductInWishlist(Long productId, String userEmail);
}