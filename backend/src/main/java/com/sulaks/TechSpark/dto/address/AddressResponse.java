package com.sulaks.TechSpark.dto.address;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AddressResponse {

    private Long addressId;
    private Long userId;
    private String fullName;
    private String phone;
    private String line1;
    private String line2;
    private String city;
    private String district;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private LocalDateTime createdAt;
}