package com.sulaks.TechSpark.dto.variant_attribute_value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantAttributeValueRequest {

    @NotNull(message = "Product variant id is required")
    private Long productVariantId;

    @NotNull(message = "Variant attribute id is required")
    private Long variantAttributeId;

    @NotBlank(message = "Value is required")
    @Size(max = 120, message = "Value must not exceed 120 characters")
    private String value;
}