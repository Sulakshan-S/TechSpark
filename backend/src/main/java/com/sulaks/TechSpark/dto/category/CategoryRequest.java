package com.sulaks.TechSpark.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 120, message = "Category name must not exceed 120 characters")
    private String name;

    @NotBlank(message = "Category slug is required")
    @Size(max = 140, message = "Category slug must not exceed 140 characters")
    private String slug;

    private Boolean isActive = true;

    private Long parentCategoryId;
}