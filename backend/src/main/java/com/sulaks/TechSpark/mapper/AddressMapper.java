package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.address.AddressRequest;
import com.sulaks.TechSpark.dto.address.AddressResponse;
import com.sulaks.TechSpark.models.Address;
import com.sulaks.TechSpark.models.User;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toAddress(AddressRequest request, User user) {
        return Address.builder()
                .user(user)
                .fullName(request.getFullName().trim())
                .phone(request.getPhone().trim())
                .line1(request.getLine1().trim())
                .line2(request.getLine2() != null ? request.getLine2().trim() : null)
                .city(request.getCity().trim())
                .district(request.getDistrict().trim())
                .postalCode(request.getPostalCode().trim())
                .country(
                        request.getCountry() != null && !request.getCountry().trim().isEmpty()
                                ? request.getCountry().trim()
                                : "Sri Lanka"
                )
                .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                .build();
    }

    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .userId(address.getUser().getUserId())
                .fullName(address.getFullName())
                .phone(address.getPhone())
                .line1(address.getLine1())
                .line2(address.getLine2())
                .city(address.getCity())
                .district(address.getDistrict())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .isDefault(address.isDefault())
                .createdAt(address.getCreatedAt())
                .build();
    }
}