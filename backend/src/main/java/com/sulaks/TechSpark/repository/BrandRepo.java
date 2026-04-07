package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepo extends JpaRepository<Brand, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
    Optional<Brand> findBySlug(String slug);
    Optional<Brand> findByNameIgnoreCase(String name);
    List<Brand> findByNameContainingIgnoreCase(String name);
}