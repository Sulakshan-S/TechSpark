package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.brand.BrandRequest;
import com.sulaks.TechSpark.dto.brand.BrandResponse;
import com.sulaks.TechSpark.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandRequest request) {
        return ResponseEntity.ok(brandService.createBrand(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BrandResponse>> searchBrands(@RequestParam String keyword) {
        return ResponseEntity.ok(brandService.searchBrands(keyword));
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable Long brandId) {
        return ResponseEntity.ok(brandService.getBrandById(brandId));
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable Long brandId,
            @Valid @RequestBody BrandRequest request
    ) {
        return ResponseEntity.ok(brandService.updateBrand(brandId, request));
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok("Brand deleted successfully");
    }
}