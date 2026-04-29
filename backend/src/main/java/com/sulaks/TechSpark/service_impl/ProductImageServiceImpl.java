package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.product_img.ProductImageRequest;
import com.sulaks.TechSpark.dto.product_img.ProductImageResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ProductImageMapper;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.models.ProductImage;
import com.sulaks.TechSpark.repository.ProductImageRepo;
import com.sulaks.TechSpark.repository.ProductRepo;
import com.sulaks.TechSpark.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepo productImageRepo;
    private final ProductRepo productRepo;
    private final ProductImageMapper productImageMapper;

    @Override
    public ProductImageResponse createProductImage(ProductImageRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + request.getProductId())
                );

        String imageUrl = request.getUrl().trim();

        if (productImageRepo.existsByProduct_ProductIdAndUrl(product.getProductId(), imageUrl)) {
            throw new ResourceAlreadyExistsException("This image url already exists for the product");
        }

        boolean makePrimary = request.getIsPrimary() != null && request.getIsPrimary();

        if (makePrimary) {
            clearPrimaryImage(product.getProductId());
        }

        ProductImage productImage = productImageMapper.toProductImage(request, product);

        List<ProductImage> existingImages = productImageRepo.findByProduct_ProductId(product.getProductId());
        if (existingImages.isEmpty()) {
            productImage.setPrimary(true);
        }

        ProductImage savedImage = productImageRepo.save(productImage);
        return productImageMapper.toProductImageResponse(savedImage);
    }

    @Override
    public List<ProductImageResponse> getImagesByProductId(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return productImageRepo.findByProduct_ProductId(productId)
                .stream()
                .map(productImageMapper::toProductImageResponse)
                .toList();
    }

    @Override
    public ProductImageResponse getProductImageById(Long productImageId) {
        ProductImage productImage = productImageRepo.findById(productImageId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product image not found with id: " + productImageId)
                );

        return productImageMapper.toProductImageResponse(productImage);
    }

    @Override
    public ProductImageResponse getPrimaryImageByProductId(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        ProductImage productImage = productImageRepo.findByProduct_ProductIdAndIsPrimaryTrue(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Primary image not found for product id: " + productId)
                );

        return productImageMapper.toProductImageResponse(productImage);
    }

    @Override
    public ProductImageResponse updateProductImage(Long productImageId, ProductImageRequest request) {
        ProductImage existingImage = productImageRepo.findById(productImageId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product image not found with id: " + productImageId)
                );

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + request.getProductId())
                );

        String newUrl = request.getUrl().trim();

        if (!existingImage.getUrl().equals(newUrl)
                && productImageRepo.existsByProduct_ProductIdAndUrl(product.getProductId(), newUrl)) {
            throw new ResourceAlreadyExistsException("This image url already exists for the product");
        }

        boolean makePrimary = request.getIsPrimary() != null && request.getIsPrimary();

        if (makePrimary) {
            clearPrimaryImage(product.getProductId());
        }

        existingImage.setProduct(product);
        existingImage.setUrl(newUrl);
        existingImage.setPrimary(makePrimary);

        List<ProductImage> productImages = productImageRepo.findByProduct_ProductId(product.getProductId());
        boolean hasAnyPrimary = productImages.stream()
                .anyMatch(image -> !image.getProductImageId().equals(productImageId) && image.isPrimary());

        if (!hasAnyPrimary && !makePrimary) {
            existingImage.setPrimary(true);
        }

        ProductImage updatedImage = productImageRepo.save(existingImage);
        return productImageMapper.toProductImageResponse(updatedImage);
    }

    @Override
    public void deleteProductImage(Long productImageId) {
        ProductImage productImage = productImageRepo.findById(productImageId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product image not found with id: " + productImageId)
                );

        Long productId = productImage.getProduct().getProductId();
        boolean wasPrimary = productImage.isPrimary();

        productImageRepo.delete(productImage);

        if (wasPrimary) {
            List<ProductImage> remainingImages = productImageRepo.findByProduct_ProductId(productId);
            if (!remainingImages.isEmpty()) {
                ProductImage firstImage = remainingImages.get(0);
                firstImage.setPrimary(true);
                productImageRepo.save(firstImage);
            }
        }
    }

    private void clearPrimaryImage(Long productId) {
        productImageRepo.findByProduct_ProductId(productId)
                .forEach(image -> {
                    if (image.isPrimary()) {
                        image.setPrimary(false);
                        productImageRepo.save(image);
                    }
                });
    }
}