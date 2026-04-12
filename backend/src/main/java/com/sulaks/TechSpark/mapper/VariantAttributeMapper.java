package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeRequest;
import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeResponse;
import com.sulaks.TechSpark.models.VariantAttribute;
import org.springframework.stereotype.Component;

@Component
public class VariantAttributeMapper {

    public VariantAttribute toEntity(VariantAttributeRequest request) {
        return VariantAttribute.builder()
                .name(request.getName().trim())
                .build();
    }

    public VariantAttributeResponse toResponse(VariantAttribute variantAttribute) {
        return VariantAttributeResponse.builder()
                .variantAttributeId(variantAttribute.getVariantAttributeId())
                .name(variantAttribute.getName())
                .build();
    }
}