package com.sulaks.TechSpark.dto.product_variant;

import com.sulaks.TechSpark.enums.ProductVariantStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantRequest {

    @NotNull(message = "Product id is required")
    private Long productId;

    @NotBlank(message = "SKU is required")
    @Size(max = 80, message = "SKU must not exceed 80 characters")
    private String sku;

    @NotBlank(message = "Variant name is required")
    @Size(max = 200, message = "Variant name must not exceed 200 characters")
    private String variantName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount price must be 0 or greater")
    private BigDecimal discountPrice;

    private ProductVariantStatus status;
}