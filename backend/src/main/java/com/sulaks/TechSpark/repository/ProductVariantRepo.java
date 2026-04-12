package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepo extends JpaRepository<ProductVariant, Long> {

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsByProduct_ProductIdAndVariantNameIgnoreCase(Long productId, String variantName);

    List<ProductVariant> findByVariantNameContainingIgnoreCase(String keyword);

    List<ProductVariant> findByProduct_ProductId(Long productId);
}