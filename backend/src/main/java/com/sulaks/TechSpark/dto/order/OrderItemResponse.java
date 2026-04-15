package com.sulaks.TechSpark.dto.order;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderItemResponse {

    private Long orderItemId;
    private Long productVariantId;
    private String sku;
    private String variantName;
    private Long productId;
    private String productName;
    private String productSlug;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}