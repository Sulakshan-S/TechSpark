package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.brand.BrandRequest;
import com.sulaks.TechSpark.dto.brand.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(BrandRequest request);
    List<BrandResponse> getAllBrands();
    BrandResponse getBrandById(Long brandId);
    BrandResponse updateBrand(Long brandId, BrandRequest request);
    void deleteBrand(Long brandId);
    BrandResponse getBrandByName(String name);
    List<BrandResponse> searchBrands(String keyword);
}