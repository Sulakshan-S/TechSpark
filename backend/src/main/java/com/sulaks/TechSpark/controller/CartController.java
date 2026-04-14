package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.cart.CartAddRequest;
import com.sulaks.TechSpark.dto.cart.CartResponse;
import com.sulaks.TechSpark.dto.cart.CartUpdateQuantityRequest;
import com.sulaks.TechSpark.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart(Authentication authentication) {
        return ResponseEntity.ok(
                cartService.getMyCart(authentication.getName())
        );
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @Valid @RequestBody CartAddRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                cartService.addItemToCart(request, authentication.getName())
        );
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartUpdateQuantityRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                cartService.updateCartItemQuantity(cartItemId, request, authentication.getName())
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication
    ) {
        cartService.removeCartItem(cartItemId, authentication.getName());
        return ResponseEntity.ok("Cart item removed successfully");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.ok("Cart cleared successfully");
    }
}