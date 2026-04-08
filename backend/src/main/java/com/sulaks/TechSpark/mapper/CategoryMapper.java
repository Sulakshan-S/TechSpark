package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.category.CategoryRequest;
import com.sulaks.TechSpark.dto.category.CategoryResponse;
import com.sulaks.TechSpark.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request, Category parentCategory) {
        return Category.builder()
                .name(request.getName().trim())
                .slug(request.getSlug().trim().toLowerCase())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .parentCategory(parentCategory)
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .slug(category.getSlug())
                .isActive(category.isActive())
                .parentCategoryId(
                        category.getParentCategory() != null
                                ? category.getParentCategory().getCategoryId()
                                : null
                )
                .parentCategoryName(
                        category.getParentCategory() != null
                                ? category.getParentCategory().getName()
                                : null
                )
                .build();
    }
}