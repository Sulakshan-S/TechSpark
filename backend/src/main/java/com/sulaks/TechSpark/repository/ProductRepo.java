package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);

    Optional<Product> findByNameIgnoreCase(String name);

    Optional<Product> findBySlug(String slug);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategory_CategoryId(Long categoryId);

    List<Product> findByBrand_BrandId(Long brandId);
}