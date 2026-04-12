package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantAttributeRepo extends JpaRepository<VariantAttribute, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<VariantAttribute> findByNameIgnoreCase(String name);

    List<VariantAttribute> findByNameContainingIgnoreCase(String keyword);
}