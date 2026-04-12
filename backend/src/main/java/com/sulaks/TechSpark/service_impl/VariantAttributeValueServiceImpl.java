package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueRequest;
import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.VariantAttributeValueMapper;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.VariantAttribute;
import com.sulaks.TechSpark.models.VariantAttributeValue;
import com.sulaks.TechSpark.repository.VariantAttributeRepo;
import com.sulaks.TechSpark.repository.VariantAttributeValueRepo;
import com.sulaks.TechSpark.service.VariantAttributeValueService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantAttributeValueServiceImpl implements VariantAttributeValueService {

    private final VariantAttributeValueRepo variantAttributeValueRepo;
    private final VariantAttributeRepo variantAttributeRepo;
    private final VariantAttributeValueMapper variantAttributeValueMapper;
    private final EntityManager entityManager;

    @Override
    public VariantAttributeValueResponse createVariantAttributeValue(VariantAttributeValueRequest request) {
        ProductVariant productVariant = entityManager.find(ProductVariant.class, request.getProductVariantId());
        if (productVariant == null) {
            throw new ResourceNotFoundException(
                    "Product variant not found with id: " + request.getProductVariantId()
            );
        }

        VariantAttribute variantAttribute = variantAttributeRepo.findById(request.getVariantAttributeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant attribute not found with id: " + request.getVariantAttributeId()
                        )
                );

        if (variantAttributeValueRepo
                .existsByProductVariant_ProductVariantIdAndVariantAttribute_VariantAttributeId(
                        request.getProductVariantId(),
                        request.getVariantAttributeId()
                )) {
            throw new ResourceAlreadyExistsException(
                    "This variant already has a value for the selected attribute"
            );
        }

        VariantAttributeValue variantAttributeValue = variantAttributeValueMapper.toEntity(
                request, productVariant, variantAttribute
        );

        VariantAttributeValue savedValue = variantAttributeValueRepo.save(variantAttributeValue);
        return variantAttributeValueMapper.toResponse(savedValue);
    }

    @Override
    public List<VariantAttributeValueResponse> getAllVariantAttributeValues() {
        return variantAttributeValueRepo.findAll()
                .stream()
                .map(variantAttributeValueMapper::toResponse)
                .toList();
    }

    @Override
    public VariantAttributeValueResponse getVariantAttributeValueById(Long variantAttributeValueId) {
        VariantAttributeValue variantAttributeValue = variantAttributeValueRepo.findById(variantAttributeValueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant attribute value not found with id: " + variantAttributeValueId
                        )
                );

        return variantAttributeValueMapper.toResponse(variantAttributeValue);
    }

    @Override
    public List<VariantAttributeValueResponse> getValuesByProductVariant(Long productVariantId) {
        return variantAttributeValueRepo.findByProductVariant_ProductVariantId(productVariantId)
                .stream()
                .map(variantAttributeValueMapper::toResponse)
                .toList();
    }

    @Override
    public List<VariantAttributeValueResponse> getValuesByVariantAttribute(Long variantAttributeId) {
        return variantAttributeValueRepo.findByVariantAttribute_VariantAttributeId(variantAttributeId)
                .stream()
                .map(variantAttributeValueMapper::toResponse)
                .toList();
    }

    @Override
    public VariantAttributeValueResponse updateVariantAttributeValue(
            Long variantAttributeValueId,
            VariantAttributeValueRequest request
    ) {
        VariantAttributeValue existingValue = variantAttributeValueRepo.findById(variantAttributeValueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant attribute value not found with id: " + variantAttributeValueId
                        )
                );

        ProductVariant productVariant = entityManager.find(ProductVariant.class, request.getProductVariantId());
        if (productVariant == null) {
            throw new ResourceNotFoundException(
                    "Product variant not found with id: " + request.getProductVariantId()
            );
        }

        VariantAttribute variantAttribute = variantAttributeRepo.findById(request.getVariantAttributeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant attribute not found with id: " + request.getVariantAttributeId()
                        )
                );

        boolean variantChanged = !existingValue.getProductVariant().getProductVariantId()
                .equals(request.getProductVariantId());

        boolean attributeChanged = !existingValue.getVariantAttribute().getVariantAttributeId()
                .equals(request.getVariantAttributeId());

        if ((variantChanged || attributeChanged)
                && variantAttributeValueRepo
                .existsByProductVariant_ProductVariantIdAndVariantAttribute_VariantAttributeId(
                        request.getProductVariantId(),
                        request.getVariantAttributeId()
                )) {
            throw new ResourceAlreadyExistsException(
                    "This variant already has a value for the selected attribute"
            );
        }

        existingValue.setProductVariant(productVariant);
        existingValue.setVariantAttribute(variantAttribute);
        existingValue.setValue(request.getValue().trim());

        VariantAttributeValue updatedValue = variantAttributeValueRepo.save(existingValue);
        return variantAttributeValueMapper.toResponse(updatedValue);
    }

    @Override
    public void deleteVariantAttributeValue(Long variantAttributeValueId) {
        VariantAttributeValue variantAttributeValue = variantAttributeValueRepo.findById(variantAttributeValueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant attribute value not found with id: " + variantAttributeValueId
                        )
                );

        variantAttributeValueRepo.delete(variantAttributeValue);
    }
}