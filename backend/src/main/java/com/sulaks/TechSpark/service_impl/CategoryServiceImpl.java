package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.category.CategoryRequest;
import com.sulaks.TechSpark.dto.category.CategoryResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.CategoryMapper;
import com.sulaks.TechSpark.models.Category;
import com.sulaks.TechSpark.repository.CategoryRepo;
import com.sulaks.TechSpark.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepo.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ResourceAlreadyExistsException("Category name already exists");
        }

        if (categoryRepo.existsBySlug(request.getSlug().trim().toLowerCase())) {
            throw new ResourceAlreadyExistsException("Category slug already exists");
        }

        Category parentCategory = null;

        if (request.getParentCategoryId() != null) {
            parentCategory = categoryRepo.findById(request.getParentCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Parent category not found with id: " + request.getParentCategoryId()
                            )
                    );
        }

        Category category = categoryMapper.toCategory(request, parentCategory);
        Category savedCategory = categoryRepo.save(category);

        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepo.findByIsActiveTrue()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + categoryId)
                );

        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepo.findByNameIgnoreCase(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with name: " + name)
                );

        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + categoryId)
                );

        String newName = request.getName().trim();
        String newSlug = request.getSlug().trim().toLowerCase();

        if (!category.getName().equalsIgnoreCase(newName)
                && categoryRepo.existsByNameIgnoreCase(newName)) {
            throw new ResourceAlreadyExistsException("Category name already exists");
        }

        if (!category.getSlug().equals(newSlug)
                && categoryRepo.existsBySlug(newSlug)) {
            throw new ResourceAlreadyExistsException("Category slug already exists");
        }

        Category parentCategory = null;

        if (request.getParentCategoryId() != null) {
            parentCategory = categoryRepo.findById(request.getParentCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Parent category not found with id: " + request.getParentCategoryId()
                            )
                    );

            if (parentCategory.getCategoryId().equals(categoryId)) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }
        }

        category.setName(newName);
        category.setSlug(newSlug);
        category.setActive(request.getIsActive() != null ? request.getIsActive() : category.isActive());
        category.setParentCategory(parentCategory);

        Category updatedCategory = categoryRepo.save(category);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + categoryId)
                );

        categoryRepo.delete(category);
    }

    @Override
    public List<CategoryResponse> searchCategories(String keyword) {
        return categoryRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }
}