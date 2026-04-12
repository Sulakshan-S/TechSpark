package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.product_variant.ProductVariantRequest;
import com.sulaks.TechSpark.dto.product_variant.ProductVariantResponse;
import com.sulaks.TechSpark.enums.ProductVariantStatus;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.models.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariant toProductVariant(ProductVariantRequest request, Product product) {
        return ProductVariant.builder()
                .product(product)
                .sku(request.getSku().trim())
                .variantName(request.getVariantName().trim())
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .status(request.getStatus() != null ? request.getStatus() : ProductVariantStatus.ACTIVE)
                .build();
    }

    public ProductVariantResponse toProductVariantResponse(ProductVariant productVariant) {
        return ProductVariantResponse.builder()
                .productVariantId(productVariant.getProductVariantId())
                .productId(productVariant.getProduct().getProductId())
                .productName(productVariant.getProduct().getName())
                .sku(productVariant.getSku())
                .variantName(productVariant.getVariantName())
                .price(productVariant.getPrice())
                .discountPrice(productVariant.getDiscountPrice())
                .status(productVariant.getStatus())
                .build();
    }
}