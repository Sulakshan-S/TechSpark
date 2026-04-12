package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueRequest;
import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueResponse;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.VariantAttribute;
import com.sulaks.TechSpark.models.VariantAttributeValue;
import org.springframework.stereotype.Component;

@Component
public class VariantAttributeValueMapper {

    public VariantAttributeValue toEntity(
            VariantAttributeValueRequest request,
            ProductVariant productVariant,
            VariantAttribute variantAttribute
    ) {
        return VariantAttributeValue.builder()
                .productVariant(productVariant)
                .variantAttribute(variantAttribute)
                .value(request.getValue().trim())
                .build();
    }

    public VariantAttributeValueResponse toResponse(VariantAttributeValue variantAttributeValue) {
        return VariantAttributeValueResponse.builder()
                .variantAttributeValueId(variantAttributeValue.getVariantAttributeValueId())
                .productVariantId(variantAttributeValue.getProductVariant().getProductVariantId())
                .productVariantName(variantAttributeValue.getProductVariant().getVariantName())
                .variantAttributeId(variantAttributeValue.getVariantAttribute().getVariantAttributeId())
                .variantAttributeName(variantAttributeValue.getVariantAttribute().getName())
                .value(variantAttributeValue.getValue())
                .build();
    }
}