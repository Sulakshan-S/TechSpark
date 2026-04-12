package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.VariantInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantInventoryRepo extends JpaRepository<VariantInventory, Long> {
}