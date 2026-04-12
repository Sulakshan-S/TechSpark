package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeRequest;
import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.VariantAttributeMapper;
import com.sulaks.TechSpark.models.VariantAttribute;
import com.sulaks.TechSpark.repository.VariantAttributeRepo;
import com.sulaks.TechSpark.service.VariantAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantAttributeServiceImpl implements VariantAttributeService {

    private final VariantAttributeRepo variantAttributeRepo;
    private final VariantAttributeMapper variantAttributeMapper;

    @Override
    public VariantAttributeResponse createVariantAttribute(VariantAttributeRequest request) {
        String attributeName = request.getName().trim();

        if (variantAttributeRepo.existsByNameIgnoreCase(attributeName)) {
            throw new ResourceAlreadyExistsException(
                    "Variant attribute already exists: " + attributeName
            );
        }

        VariantAttribute variantAttribute = variantAttributeMapper.toEntity(request);
        VariantAttribute savedVariantAttribute = variantAttributeRepo.save(variantAttribute);

        return variantAttributeMapper.toResponse(savedVariantAttribute);
    }

    @Override
    public List<VariantAttributeResponse> getAllVariantAttributes() {
        return variantAttributeRepo.findAll()
                .stream()
                .map(variantAttributeMapper::toResponse)
                .toList();
    }

    @Override
    public VariantAttributeResponse getVariantAttributeById(Long variantAttributeId) {
        VariantAttribute variantAttribute = variantAttributeRepo.findById(variantAttributeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Variant attribute not found with id: " + variantAttributeId)
                );

        return variantAttributeMapper.toResponse(variantAttribute);
    }

    @Override
    public VariantAttributeResponse updateVariantAttribute(Long variantAttributeId, VariantAttributeRequest request) {
        VariantAttribute variantAttribute = variantAttributeRepo.findById(variantAttributeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Variant attribute not found with id: " + variantAttributeId)
                );

        String newName = request.getName().trim();

        if (!variantAttribute.getName().equalsIgnoreCase(newName)
                && variantAttributeRepo.existsByNameIgnoreCase(newName)) {
            throw new ResourceAlreadyExistsException(
                    "Variant attribute already exists: " + newName
            );
        }

        variantAttribute.setName(newName);

        VariantAttribute updatedVariantAttribute = variantAttributeRepo.save(variantAttribute);
        return variantAttributeMapper.toResponse(updatedVariantAttribute);
    }

    @Override
    public void deleteVariantAttribute(Long variantAttributeId) {
        VariantAttribute variantAttribute = variantAttributeRepo.findById(variantAttributeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Variant attribute not found with id: " + variantAttributeId)
                );

        variantAttributeRepo.delete(variantAttribute);
    }

    @Override
    public List<VariantAttributeResponse> searchVariantAttributes(String keyword) {
        return variantAttributeRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(variantAttributeMapper::toResponse)
                .toList();
    }
}