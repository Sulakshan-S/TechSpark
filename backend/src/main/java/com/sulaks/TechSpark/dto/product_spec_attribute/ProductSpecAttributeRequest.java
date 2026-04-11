package com.sulaks.TechSpark.dto.product_spec_attribute;

import com.sulaks.TechSpark.enums.SpecDataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSpecAttributeRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotBlank(message = "Attribute name is required")
    @Size(max = 120, message = "Attribute name must not exceed 120 characters")
    private String name;

    @NotNull(message = "Data type is required")
    private SpecDataType dataType;

    private Boolean isRequired = false;

    private Boolean isFilterable = false;

    private Boolean isActive = true;
}