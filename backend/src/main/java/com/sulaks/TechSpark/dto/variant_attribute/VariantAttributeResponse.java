package com.sulaks.TechSpark.dto.variant_attribute;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VariantAttributeResponse {

    private Long variantAttributeId;
    private String name;
}