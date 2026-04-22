package com.sulaks.TechSpark.dto.return_request;

import com.sulaks.TechSpark.enums.ConditionStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReturnItemRequest {

    @NotNull(message = "Order item id is required")
    private Long orderItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Condition status is required")
    private ConditionStatus conditionStatus;
}