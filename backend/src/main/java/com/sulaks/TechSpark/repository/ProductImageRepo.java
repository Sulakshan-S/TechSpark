package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProduct_ProductId(Long productId);

    Optional<ProductImage> findByProduct_ProductIdAndIsPrimaryTrue(Long productId);

    boolean existsByProduct_ProductIdAndUrl(Long productId, String url);
}