package com.sulaks.TechSpark.dto.variant_attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantAttributeRequest {

    @NotBlank(message = "Variant attribute name is required")
    @Size(max = 80, message = "Variant attribute name must not exceed 80 characters")
    private String name;
}