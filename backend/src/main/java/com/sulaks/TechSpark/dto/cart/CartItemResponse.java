package com.sulaks.TechSpark.dto.cart;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CartItemResponse {

    private Long cartItemId;

    private Long productVariantId;
    private String sku;
    private String variantName;

    private Long productId;
    private String productName;
    private String productSlug;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

    private LocalDateTime createdAt;
}