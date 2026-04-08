package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.category.CategoryRequest;
import com.sulaks.TechSpark.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getActiveCategories();

    CategoryResponse getCategoryById(Long categoryId);

    CategoryResponse getCategoryByName(String name);

    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);

    void deleteCategory(Long categoryId);

    List<CategoryResponse> searchCategories(String keyword);
}