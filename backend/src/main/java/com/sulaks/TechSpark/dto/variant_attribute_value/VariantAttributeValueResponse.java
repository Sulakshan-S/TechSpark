package com.sulaks.TechSpark.dto.variant_attribute_value;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VariantAttributeValueResponse {

    private Long variantAttributeValueId;

    private Long productVariantId;
    private String productVariantName;

    private Long variantAttributeId;
    private String variantAttributeName;

    private String value;
}