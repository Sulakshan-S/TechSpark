package com.sulaks.TechSpark.dto.product;

import com.sulaks.TechSpark.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private Long brandId;

    @NotBlank(message = "Product name is required")
    @Size(max = 180, message = "Product name must not exceed 180 characters")
    private String name;

    @NotBlank(message = "Product slug is required")
    @Size(max = 200, message = "Product slug must not exceed 200 characters")
    private String slug;

    private String description;

    private ProductStatus status = ProductStatus.ACTIVE;
}