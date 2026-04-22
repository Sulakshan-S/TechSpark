package com.sulaks.TechSpark.dto.return_request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReturnRequest {

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String comments;

    @Valid
    @NotEmpty(message = "At least one return item is required")
    private List<CreateReturnItemRequest> items;
}