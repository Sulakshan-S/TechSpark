package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);

    Optional<Category> findByNameIgnoreCase(String name);

    Optional<Category> findBySlug(String slug);

    List<Category> findByNameContainingIgnoreCase(String keyword);

    List<Category> findByIsActiveTrue();
}