package com.sulaks.TechSpark.dto.product_spec_attribute;

import com.sulaks.TechSpark.enums.SpecDataType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSpecAttributeResponse {

    private Long productSpecAttributeId;
    private Long categoryId;
    private String categoryName;
    private String name;
    private SpecDataType dataType;
    private boolean isRequired;
    private boolean isFilterable;
    private boolean isActive;
}