package com.sulaks.TechSpark.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRequest {

    @NotBlank(message = "Brand name is required")
    @Size(max = 120, message = "Brand name must not exceed 120 characters")
    private String name;

    @NotBlank(message = "Brand slug is required")
    @Size(max = 140, message = "Brand slug must not exceed 140 characters")
    private String slug;

    private Boolean isActive = true;
}