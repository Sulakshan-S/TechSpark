package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.wishlist.WishlistRequest;
import com.sulaks.TechSpark.dto.wishlist.WishlistResponse;
import com.sulaks.TechSpark.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(
            @Valid @RequestBody WishlistRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                wishlistService.addToWishlist(request, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getMyWishlist(Authentication authentication) {
        return ResponseEntity.ok(
                wishlistService.getMyWishlist(authentication.getName())
        );
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<Map<String, Boolean>> isProductInWishlist(
            @PathVariable Long productId,
            Authentication authentication
    ) {
        boolean inWishlist = wishlistService.isProductInWishlist(productId, authentication.getName());
        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }

    @DeleteMapping("/{wishlistItemId}")
    public ResponseEntity<String> removeWishlistItem(
            @PathVariable Long wishlistItemId,
            Authentication authentication
    ) {
        wishlistService.removeWishlistItem(wishlistItemId, authentication.getName());
        return ResponseEntity.ok("Wishlist item removed successfully");
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> removeFromWishlistByProduct(
            @PathVariable Long productId,
            Authentication authentication
    ) {
        wishlistService.removeFromWishlistByProduct(productId, authentication.getName());
        return ResponseEntity.ok("Product removed from wishlist successfully");
    }
}