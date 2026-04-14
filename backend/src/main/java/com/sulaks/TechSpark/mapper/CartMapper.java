package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.cart.CartItemResponse;
import com.sulaks.TechSpark.dto.cart.CartResponse;
import com.sulaks.TechSpark.models.Cart;
import com.sulaks.TechSpark.models.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        BigDecimal subTotal = cartItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .productVariantId(cartItem.getProductVariant().getProductVariantId())
                .sku(cartItem.getProductVariant().getSku())
                .variantName(cartItem.getProductVariant().getVariantName())
                .productId(cartItem.getProductVariant().getProduct().getProductId())
                .productName(cartItem.getProductVariant().getProduct().getName())
                .productSlug(cartItem.getProductVariant().getProduct().getSlug())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .subTotal(subTotal)
                .createdAt(cartItem.getCreatedAt())
                .build();
    }

    public CartResponse toCartResponse(Cart cart, List<CartItem> cartItems) {
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(this::toCartItemResponse)
                .toList();

        int totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .userName(cart.getUser().getFullName())
                .status(cart.getStatus())
                .items(itemResponses)
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}