package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeRequest;
import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ProductSpecAttributeMapper;
import com.sulaks.TechSpark.models.Category;
import com.sulaks.TechSpark.models.ProductSpecAttribute;
import com.sulaks.TechSpark.repository.CategoryRepo;
import com.sulaks.TechSpark.repository.ProductSpecAttributeRepo;
import com.sulaks.TechSpark.service.ProductSpecAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecAttributeServiceImpl implements ProductSpecAttributeService {

    private final ProductSpecAttributeRepo productSpecAttributeRepo;
    private final CategoryRepo categoryRepo;
    private final ProductSpecAttributeMapper productSpecAttributeMapper;

    @Override
    public ProductSpecAttributeResponse createProductSpecAttribute(ProductSpecAttributeRequest request) {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + request.getCategoryId())
                );

        String attributeName = request.getName().trim();

        if (productSpecAttributeRepo.existsByCategory_CategoryIdAndNameIgnoreCase(category.getCategoryId(), attributeName)) {
            throw new ResourceAlreadyExistsException(
                    "Spec attribute already exists in this category: " + attributeName
            );
        }

        ProductSpecAttribute attribute = productSpecAttributeMapper.toEntity(request, category);
        ProductSpecAttribute savedAttribute = productSpecAttributeRepo.save(attribute);

        return productSpecAttributeMapper.toResponse(savedAttribute);
    }

    @Override
    public List<ProductSpecAttributeResponse> getAllProductSpecAttributes() {
        return productSpecAttributeRepo.findAll()
                .stream()
                .map(productSpecAttributeMapper::toResponse)
                .toList();
    }

    @Override
    public ProductSpecAttributeResponse getProductSpecAttributeById(Long productSpecAttributeId) {
        ProductSpecAttribute attribute = productSpecAttributeRepo.findById(productSpecAttributeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product spec attribute not found with id: " + productSpecAttributeId)
                );

        return productSpecAttributeMapper.toResponse(attribute);
    }

    @Override
    public ProductSpecAttributeResponse updateProductSpecAttribute(Long productSpecAttributeId, ProductSpecAttributeRequest request) {
        ProductSpecAttribute attribute = productSpecAttributeRepo.findById(productSpecAttributeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product spec attribute not found with id: " + productSpecAttributeId)
                );

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + request.getCategoryId())
                );

        String newName = request.getName().trim();

        boolean nameChanged = !attribute.getName().equalsIgnoreCase(newName);
        boolean categoryChanged = !attribute.getCategory().getCategoryId().equals(request.getCategoryId());

        if ((nameChanged || categoryChanged)
                && productSpecAttributeRepo.existsByCategory_CategoryIdAndNameIgnoreCase(category.getCategoryId(), newName)) {
            throw new ResourceAlreadyExistsException(
                    "Spec attribute already exists in this category: " + newName
            );
        }

        attribute.setCategory(category);
        attribute.setName(newName);
        attribute.setDataType(request.getDataType());
        attribute.setRequired(request.getIsRequired() != null ? request.getIsRequired() : attribute.isRequired());
        attribute.setFilterable(request.getIsFilterable() != null ? request.getIsFilterable() : attribute.isFilterable());
        attribute.setActive(request.getIsActive() != null ? request.getIsActive() : attribute.isActive());

        ProductSpecAttribute updatedAttribute = productSpecAttributeRepo.save(attribute);
        return productSpecAttributeMapper.toResponse(updatedAttribute);
    }

    @Override
    public void deleteProductSpecAttribute(Long productSpecAttributeId) {
        ProductSpecAttribute attribute = productSpecAttributeRepo.findById(productSpecAttributeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product spec attribute not found with id: " + productSpecAttributeId)
                );

        productSpecAttributeRepo.delete(attribute);
    }

    @Override
    public List<ProductSpecAttributeResponse> getProductSpecAttributesByCategory(Long categoryId) {
        return productSpecAttributeRepo.findByCategory_CategoryId(categoryId)
                .stream()
                .map(productSpecAttributeMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductSpecAttributeResponse> getActiveProductSpecAttributesByCategory(Long categoryId) {
        return productSpecAttributeRepo.findByCategory_CategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(productSpecAttributeMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductSpecAttributeResponse> searchProductSpecAttributes(String keyword) {
        return productSpecAttributeRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(productSpecAttributeMapper::toResponse)
                .toList();
    }
}