package com.sulaks.TechSpark.dto.stock_movement;

import com.sulaks.TechSpark.enums.StockMovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockMovementRequest {

    @NotNull(message = "Product variant id is required")
    private Long productVariantId;

    @NotNull(message = "Movement type is required")
    private StockMovementType movementType;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @Size(max = 120, message = "Reason must not exceed 120 characters")
    private String reason;

    @Size(max = 60, message = "Reference type must not exceed 60 characters")
    private String referenceType;

    private Long referenceId;
}