package com.sulaks.TechSpark.dto.product;

import com.sulaks.TechSpark.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private Long productId;
    private String name;
    private String slug;
    private String description;
    private ProductStatus status;

    private Long categoryId;
    private String categoryName;

    private Long brandId;
    private String brandName;
}