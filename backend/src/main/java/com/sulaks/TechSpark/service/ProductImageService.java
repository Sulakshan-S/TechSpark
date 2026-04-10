package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.productimage.ProductImageRequest;
import com.sulaks.TechSpark.dto.productimage.ProductImageResponse;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse createProductImage(ProductImageRequest request);

    List<ProductImageResponse> getImagesByProductId(Long productId);

    ProductImageResponse getProductImageById(Long productImageId);

    ProductImageResponse getPrimaryImageByProductId(Long productId);

    ProductImageResponse updateProductImage(Long productImageId, ProductImageRequest request);

    void deleteProductImage(Long productImageId);
}