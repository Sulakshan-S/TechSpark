package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueRequest;
import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueResponse;

import java.util.List;

public interface VariantAttributeValueService {

    VariantAttributeValueResponse createVariantAttributeValue(VariantAttributeValueRequest request);

    List<VariantAttributeValueResponse> getAllVariantAttributeValues();

    VariantAttributeValueResponse getVariantAttributeValueById(Long variantAttributeValueId);

    List<VariantAttributeValueResponse> getValuesByProductVariant(Long productVariantId);

    List<VariantAttributeValueResponse> getValuesByVariantAttribute(Long variantAttributeId);

    VariantAttributeValueResponse updateVariantAttributeValue(
            Long variantAttributeValueId,
            VariantAttributeValueRequest request
    );

    void deleteVariantAttributeValue(Long variantAttributeValueId);
}