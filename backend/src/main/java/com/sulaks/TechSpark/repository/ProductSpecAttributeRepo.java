package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.ProductSpecAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSpecAttributeRepo extends JpaRepository<ProductSpecAttribute, Long> {

    boolean existsByCategory_CategoryIdAndNameIgnoreCase(Long categoryId, String name);

    List<ProductSpecAttribute> findByCategory_CategoryId(Long categoryId);

    List<ProductSpecAttribute> findByCategory_CategoryIdAndActiveTrue(Long categoryId);

    List<ProductSpecAttribute> findByNameContainingIgnoreCase(String keyword);

    Optional<ProductSpecAttribute> findByCategory_CategoryIdAndNameIgnoreCase(Long categoryId, String name);
}