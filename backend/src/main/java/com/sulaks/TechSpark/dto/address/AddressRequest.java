package com.sulaks.TechSpark.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name must not exceed 120 characters")
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Size(max = 30, message = "Phone must not exceed 30 characters")
    private String phone;

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 180, message = "Address line 1 must not exceed 180 characters")
    private String line1;

    @Size(max = 180, message = "Address line 2 must not exceed 180 characters")
    private String line2;

    @NotBlank(message = "City is required")
    @Size(max = 80, message = "City must not exceed 80 characters")
    private String city;

    @NotBlank(message = "District is required")
    @Size(max = 80, message = "District must not exceed 80 characters")
    private String district;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 80, message = "Country must not exceed 80 characters")
    private String country;

    private Boolean isDefault = false;
}