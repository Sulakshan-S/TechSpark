package com.sulaks.TechSpark.dto.variant_inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantInventoryRequest {

    @NotNull(message = "Product variant id is required")
    private Long productVariantId;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be 0 or greater")
    private Integer stockQty;

    @NotNull(message = "Reserved quantity is required")
    @Min(value = 0, message = "Reserved quantity must be 0 or greater")
    private Integer reservedQty;
}