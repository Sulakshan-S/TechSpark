package com.sulaks.TechSpark.dto.stock_movement;

import com.sulaks.TechSpark.enums.StockMovementType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StockMovementResponse {

    private Long stockMovementId;

    private Long productVariantId;
    private String productVariantName;

    private StockMovementType movementType;
    private Integer quantity;
    private String reason;
    private String referenceType;
    private Long referenceId;

    private Long createdByUserId;
    private String createdByUserName;

}