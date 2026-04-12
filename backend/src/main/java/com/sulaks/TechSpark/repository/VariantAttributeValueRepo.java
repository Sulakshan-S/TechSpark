package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.VariantAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantAttributeValueRepo extends JpaRepository<VariantAttributeValue, Long> {

    boolean existsByProductVariant_ProductVariantIdAndVariantAttribute_VariantAttributeId(
            Long productVariantId,
            Long variantAttributeId
    );

    Optional<VariantAttributeValue> findByProductVariant_ProductVariantIdAndVariantAttribute_VariantAttributeId(
            Long productVariantId,
            Long variantAttributeId
    );

    List<VariantAttributeValue> findByProductVariant_ProductVariantId(Long productVariantId);

    List<VariantAttributeValue> findByVariantAttribute_VariantAttributeId(Long variantAttributeId);
}