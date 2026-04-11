package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeRequest;
import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeResponse;
import com.sulaks.TechSpark.models.Category;
import com.sulaks.TechSpark.models.ProductSpecAttribute;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecAttributeMapper {

    public ProductSpecAttribute toEntity(ProductSpecAttributeRequest request, Category category) {
        return ProductSpecAttribute.builder()
                .category(category)
                .name(request.getName().trim())
                .dataType(request.getDataType())
                .required(request.getIsRequired() != null ? request.getIsRequired() : false)
                .filterable(request.getIsFilterable() != null ? request.getIsFilterable() : false)
                .active(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    public ProductSpecAttributeResponse toResponse(ProductSpecAttribute attribute) {
        return ProductSpecAttributeResponse.builder()
                .productSpecAttributeId(attribute.getProductSpecAttributeId())
                .categoryId(attribute.getCategory().getCategoryId())
                .categoryName(attribute.getCategory().getName())
                .name(attribute.getName())
                .dataType(attribute.getDataType())
                .isRequired(attribute.isRequired())
                .isFilterable(attribute.isFilterable())
                .isActive(attribute.isActive())
                .build();
    }
}