package com.sulaks.TechSpark.dto.product_variant;

import com.sulaks.TechSpark.enums.ProductVariantStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductVariantResponse {

    private Long productVariantId;
    private Long productId;
    private String productName;
    private String sku;
    private String variantName;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private ProductVariantStatus status;
}