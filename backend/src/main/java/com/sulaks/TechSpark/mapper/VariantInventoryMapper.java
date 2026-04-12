package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryRequest;
import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryResponse;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.VariantInventory;
import org.springframework.stereotype.Component;

@Component
public class VariantInventoryMapper {

    public VariantInventory toEntity(
            VariantInventoryRequest request,
            ProductVariant productVariant
    ) {
        return VariantInventory.builder()
                .productVariantId(productVariant.getProductVariantId())
                .productVariant(productVariant)
                .stockQty(request.getStockQty())
                .reservedQty(request.getReservedQty())
                .build();
    }

    public VariantInventoryResponse toResponse(VariantInventory variantInventory) {
        return VariantInventoryResponse.builder()
                .productVariantId(variantInventory.getProductVariantId())
                .productVariantName(variantInventory.getProductVariant().getVariantName())
                .stockQty(variantInventory.getStockQty())
                .reservedQty(variantInventory.getReservedQty())
                .build();
    }
}