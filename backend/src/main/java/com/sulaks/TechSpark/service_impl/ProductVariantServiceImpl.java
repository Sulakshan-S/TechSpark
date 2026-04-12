package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.product_variant.ProductVariantRequest;
import com.sulaks.TechSpark.dto.product_variant.ProductVariantResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ProductVariantMapper;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.repository.ProductRepo;
import com.sulaks.TechSpark.repository.ProductVariantRepo;
import com.sulaks.TechSpark.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepo productVariantRepo;
    private final ProductRepo productRepo;
    private final ProductVariantMapper productVariantMapper;

    @Override
    public ProductVariantResponse createProductVariant(ProductVariantRequest request) {
        String sku = request.getSku().trim();
        String variantName = request.getVariantName().trim();

        if (productVariantRepo.existsBySkuIgnoreCase(sku)) {
            throw new ResourceAlreadyExistsException("SKU already exists");
        }

        if (productVariantRepo.existsByProduct_ProductIdAndVariantNameIgnoreCase(
                request.getProductId(), variantName)) {
            throw new ResourceAlreadyExistsException("Variant name already exists for this product");
        }

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + request.getProductId())
                );

        if (request.getDiscountPrice() != null
                && request.getPrice() != null
                && request.getDiscountPrice().compareTo(request.getPrice()) > 0) {
            throw new IllegalArgumentException("Discount price cannot be greater than price");
        }

        ProductVariant productVariant = productVariantMapper.toProductVariant(request, product);
        ProductVariant savedProductVariant = productVariantRepo.save(productVariant);

        return productVariantMapper.toProductVariantResponse(savedProductVariant);
    }

    @Override
    public List<ProductVariantResponse> getAllProductVariants() {
        return productVariantRepo.findAll()
                .stream()
                .map(productVariantMapper::toProductVariantResponse)
                .toList();
    }

    @Override
    public ProductVariantResponse getProductVariantById(Long productVariantId) {
        ProductVariant productVariant = productVariantRepo.findById(productVariantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product variant not found with id: " + productVariantId)
                );

        return productVariantMapper.toProductVariantResponse(productVariant);
    }

    @Override
    public ProductVariantResponse updateProductVariant(Long productVariantId, ProductVariantRequest request) {
        ProductVariant productVariant = productVariantRepo.findById(productVariantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product variant not found with id: " + productVariantId)
                );

        String newSku = request.getSku().trim();
        String newVariantName = request.getVariantName().trim();

        if (!productVariant.getSku().equalsIgnoreCase(newSku)
                && productVariantRepo.existsBySkuIgnoreCase(newSku)) {
            throw new ResourceAlreadyExistsException("SKU already exists");
        }

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + request.getProductId())
                );

        boolean variantNameChanged =
                !productVariant.getVariantName().equalsIgnoreCase(newVariantName)
                        || !productVariant.getProduct().getProductId().equals(request.getProductId());

        if (variantNameChanged && productVariantRepo.existsByProduct_ProductIdAndVariantNameIgnoreCase(
                request.getProductId(), newVariantName)) {
            throw new ResourceAlreadyExistsException("Variant name already exists for this product");
        }

        if (request.getDiscountPrice() != null
                && request.getPrice() != null
                && request.getDiscountPrice().compareTo(request.getPrice()) > 0) {
            throw new IllegalArgumentException("Discount price cannot be greater than price");
        }

        productVariant.setProduct(product);
        productVariant.setSku(newSku);
        productVariant.setVariantName(newVariantName);
        productVariant.setPrice(request.getPrice());
        productVariant.setDiscountPrice(request.getDiscountPrice());
        productVariant.setStatus(
                request.getStatus() != null ? request.getStatus() : productVariant.getStatus()
        );

        ProductVariant updatedProductVariant = productVariantRepo.save(productVariant);
        return productVariantMapper.toProductVariantResponse(updatedProductVariant);
    }

    @Override
    public void deleteProductVariant(Long productVariantId) {
        ProductVariant productVariant = productVariantRepo.findById(productVariantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product variant not found with id: " + productVariantId)
                );

        productVariantRepo.delete(productVariant);
    }

    @Override
    public List<ProductVariantResponse> searchProductVariants(String keyword) {
        return productVariantRepo.findByVariantNameContainingIgnoreCase(keyword)
                .stream()
                .map(productVariantMapper::toProductVariantResponse)
                .toList();
    }

    @Override
    public List<ProductVariantResponse> getProductVariantsByProduct(Long productId) {
        return productVariantRepo.findByProduct_ProductId(productId)
                .stream()
                .map(productVariantMapper::toProductVariantResponse)
                .toList();
    }
}