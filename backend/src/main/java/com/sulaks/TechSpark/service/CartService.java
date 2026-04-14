package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.cart.CartAddRequest;
import com.sulaks.TechSpark.dto.cart.CartResponse;
import com.sulaks.TechSpark.dto.cart.CartUpdateQuantityRequest;

public interface CartService {

    CartResponse getMyCart(String userEmail);

    CartResponse addItemToCart(CartAddRequest request, String userEmail);

    CartResponse updateCartItemQuantity(Long cartItemId, CartUpdateQuantityRequest request, String userEmail);

    void removeCartItem(Long cartItemId, String userEmail);

    void clearCart(String userEmail);
}