package com.sulaks.TechSpark.dto.cart;

import com.sulaks.TechSpark.enums.CartStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CartResponse {

    private Long cartId;

    private Long userId;
    private String userName;

    private CartStatus status;

    private List<CartItemResponse> items;

    private Integer totalItems;
    private BigDecimal totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}