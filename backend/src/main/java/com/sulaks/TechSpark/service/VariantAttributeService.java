package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeRequest;
import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeResponse;

import java.util.List;

public interface VariantAttributeService {

    VariantAttributeResponse createVariantAttribute(VariantAttributeRequest request);

    List<VariantAttributeResponse> getAllVariantAttributes();

    VariantAttributeResponse getVariantAttributeById(Long variantAttributeId);

    VariantAttributeResponse updateVariantAttribute(Long variantAttributeId, VariantAttributeRequest request);

    void deleteVariantAttribute(Long variantAttributeId);

    List<VariantAttributeResponse> searchVariantAttributes(String keyword);
}