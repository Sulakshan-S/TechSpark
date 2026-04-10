package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.productimage.ProductImageRequest;
import com.sulaks.TechSpark.dto.productimage.ProductImageResponse;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.models.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImage toProductImage(ProductImageRequest request, Product product) {
        return ProductImage.builder()
                .product(product)
                .url(request.getUrl().trim())
                .isPrimary(request.getIsPrimary() != null && request.getIsPrimary())
                .build();
    }

    public ProductImageResponse toProductImageResponse(ProductImage productImage) {
        return ProductImageResponse.builder()
                .productImageId(productImage.getProductImageId())
                .productId(productImage.getProduct().getProductId())
                .productName(productImage.getProduct().getName())
                .url(productImage.getUrl())
                .isPrimary(productImage.isPrimary())
                .createdAt(productImage.getCreatedAt())
                .build();
    }
}