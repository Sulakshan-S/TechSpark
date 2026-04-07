package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.brand.BrandRequest;
import com.sulaks.TechSpark.dto.brand.BrandResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.models.Brand;
import com.sulaks.TechSpark.repository.BrandRepo;
import com.sulaks.TechSpark.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepo brandRepo;

    @Override
    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepo.existsByNameIgnoreCase(request.getName())) {
            throw new ResourceAlreadyExistsException("Brand name already exists");
        }

        if (brandRepo.existsBySlug(request.getSlug())) {
            throw new ResourceAlreadyExistsException("Brand slug already exists");
        }

        Brand brand = Brand.builder()
                .name(request.getName().trim())
                .slug(request.getSlug().trim().toLowerCase())
                .active(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Brand savedBrand = brandRepo.save(brand);
        return mapToResponse(savedBrand);
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        return brandRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BrandResponse getBrandById(Long brandId) {
        Brand brand = brandRepo.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        return mapToResponse(brand);
    }

    @Override
    public BrandResponse updateBrand(Long brandId, BrandRequest request) {
        Brand brand = brandRepo.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        String newName = request.getName().trim();
        String newSlug = request.getSlug().trim().toLowerCase();

        if (!brand.getName().equalsIgnoreCase(newName) && brandRepo.existsByNameIgnoreCase(newName)) {
            throw new ResourceAlreadyExistsException("Brand name already exists");
        }

        if (!brand.getSlug().equals(newSlug) && brandRepo.existsBySlug(newSlug)) {
            throw new ResourceAlreadyExistsException("Brand slug already exists");
        }

        brand.setName(newName);
        brand.setSlug(newSlug);
        brand.setActive(request.getIsActive() != null ? request.getIsActive() : brand.isActive());

        Brand updatedBrand = brandRepo.save(brand);
        return mapToResponse(updatedBrand);
    }

    @Override
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepo.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        brandRepo.delete(brand);
    }

    private BrandResponse mapToResponse(Brand brand) {
        return BrandResponse.builder()
                .brandId(brand.getBrandId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .isActive(brand.isActive())
                .build();
    }

    @Override
    public BrandResponse getBrandByName(String name) {
        Brand brand = brandRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with name: " + name));

        return mapToResponse(brand);
    }

    @Override
    public List<BrandResponse> searchBrands(String keyword) {
        return brandRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}