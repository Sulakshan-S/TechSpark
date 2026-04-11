package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeRequest;
import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeResponse;

import java.util.List;

public interface ProductSpecAttributeService {

    ProductSpecAttributeResponse createProductSpecAttribute(ProductSpecAttributeRequest request);

    List<ProductSpecAttributeResponse> getAllProductSpecAttributes();

    ProductSpecAttributeResponse getProductSpecAttributeById(Long productSpecAttributeId);

    ProductSpecAttributeResponse updateProductSpecAttribute(Long productSpecAttributeId, ProductSpecAttributeRequest request);

    void deleteProductSpecAttribute(Long productSpecAttributeId);

    List<ProductSpecAttributeResponse> getProductSpecAttributesByCategory(Long categoryId);

    List<ProductSpecAttributeResponse> getActiveProductSpecAttributesByCategory(Long categoryId);

    List<ProductSpecAttributeResponse> searchProductSpecAttributes(String keyword);
}