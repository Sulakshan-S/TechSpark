package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.product.ProductRequest;
import com.sulaks.TechSpark.dto.product.ProductResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ProductMapper;
import com.sulaks.TechSpark.models.Brand;
import com.sulaks.TechSpark.models.Category;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.repository.BrandRepo;
import com.sulaks.TechSpark.repository.CategoryRepo;
import com.sulaks.TechSpark.repository.ProductRepo;
import com.sulaks.TechSpark.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final BrandRepo brandRepo;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepo.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ResourceAlreadyExistsException("Product name already exists");
        }

        if (productRepo.existsBySlug(request.getSlug().trim().toLowerCase())) {
            throw new ResourceAlreadyExistsException("Product slug already exists");
        }

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + request.getCategoryId())
                );

        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepo.findById(request.getBrandId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Brand not found with id: " + request.getBrandId())
                    );
        }

        Product product = productMapper.toProduct(request, category, brand);
        Product savedProduct = productRepo.save(product);

        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + productId)
                );

        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse getProductByName(String name) {
        Product product = productRepo.findByNameIgnoreCase(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with name: " + name)
                );

        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + productId)
                );

        String newName = request.getName().trim();
        String newSlug = request.getSlug().trim().toLowerCase();

        if (!product.getName().equalsIgnoreCase(newName)
                && productRepo.existsByNameIgnoreCase(newName)) {
            throw new ResourceAlreadyExistsException("Product name already exists");
        }

        if (!product.getSlug().equals(newSlug)
                && productRepo.existsBySlug(newSlug)) {
            throw new ResourceAlreadyExistsException("Product slug already exists");
        }

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + request.getCategoryId())
                );

        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepo.findById(request.getBrandId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Brand not found with id: " + request.getBrandId())
                    );
        }

        product.setName(newName);
        product.setSlug(newSlug);
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus() != null ? request.getStatus() : product.getStatus());
        product.setCategory(category);
        product.setBrand(brand);

        Product updatedProduct = productRepo.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + productId)
                );

        productRepo.delete(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepo.findByCategory_CategoryId(categoryId)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByBrand(Long brandId) {
        return productRepo.findByBrand_BrandId(brandId)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}