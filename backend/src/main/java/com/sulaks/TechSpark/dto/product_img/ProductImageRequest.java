package com.sulaks.TechSpark.dto.product_img;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {

    @NotNull(message = "Product id is required")
    private Long productId;

    @NotBlank(message = "Image url is required")
    @Size(max = 500, message = "Image url must not exceed 500 characters")
    private String url;

    private Boolean isPrimary = false;
}