package com.sulaks.TechSpark.dto.variant_inventory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VariantInventoryResponse {

    private Long productVariantId;
    private String productVariantName;
    private Integer stockQty;
    private Integer reservedQty;
}