package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.stock_movement.StockMovementRequest;
import com.sulaks.TechSpark.dto.stock_movement.StockMovementResponse;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.StockMovement;
import com.sulaks.TechSpark.models.User;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {

    public StockMovement toEntity(
            StockMovementRequest request,
            ProductVariant productVariant,
            User createdByUser
    ) {
        return StockMovement.builder()
                .productVariant(productVariant)
                .movementType(request.getMovementType())
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .createdByUser(createdByUser)
                .build();
    }

    public StockMovementResponse toResponse(StockMovement stockMovement) {
        return StockMovementResponse.builder()
                .stockMovementId(stockMovement.getStockMovementId())
                .productVariantId(stockMovement.getProductVariant().getProductVariantId())
                .productVariantName(stockMovement.getProductVariant().getVariantName())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .reason(stockMovement.getReason())
                .referenceType(stockMovement.getReferenceType())
                .referenceId(stockMovement.getReferenceId())
                .createdByUserId(
                        stockMovement.getCreatedByUser() != null
                                ? stockMovement.getCreatedByUser().getUserId()
                                : null
                )
                .createdByUserName(
                        stockMovement.getCreatedByUser() != null
                                ? stockMovement.getCreatedByUser().getFullName()
                                : null
                )
                .build();
    }
}