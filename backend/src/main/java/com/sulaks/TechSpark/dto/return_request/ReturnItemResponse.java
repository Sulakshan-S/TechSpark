package com.sulaks.TechSpark.dto.return_request;

import com.sulaks.TechSpark.enums.ConditionStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ReturnItemResponse {

    private Long returnItemId;

    private Long orderItemId;
    private Long productVariantId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    private ConditionStatus conditionStatus;
}