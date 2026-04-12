package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.product_variant.ProductVariantRequest;
import com.sulaks.TechSpark.dto.product_variant.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(ProductVariantRequest request);

    List<ProductVariantResponse> getAllProductVariants();

    ProductVariantResponse getProductVariantById(Long productVariantId);

    ProductVariantResponse updateProductVariant(Long productVariantId, ProductVariantRequest request);

    void deleteProductVariant(Long productVariantId);

    List<ProductVariantResponse> searchProductVariants(String keyword);

    List<ProductVariantResponse> getProductVariantsByProduct(Long productId);
}