package com.sulaks.TechSpark.dto.category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

    private Long categoryId;
    private String name;
    private String slug;
    private boolean isActive;
    private Long parentCategoryId;
    private String parentCategoryName;
}