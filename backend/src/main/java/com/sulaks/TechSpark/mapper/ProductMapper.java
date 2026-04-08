package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.product.ProductRequest;
import com.sulaks.TechSpark.dto.product.ProductResponse;
import com.sulaks.TechSpark.models.Brand;
import com.sulaks.TechSpark.models.Category;
import com.sulaks.TechSpark.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest request, Category category, Brand brand) {
        return Product.builder()
                .name(request.getName().trim())
                .slug(request.getSlug().trim().toLowerCase())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : com.sulaks.TechSpark.enums.ProductStatus.ACTIVE)
                .category(category)
                .brand(brand)
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .status(product.getStatus())
                .categoryId(product.getCategory().getCategoryId())
                .categoryName(product.getCategory().getName())
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .build();
    }
}