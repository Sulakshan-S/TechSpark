package com.sulaks.TechSpark.dto.brand;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BrandResponse {
    private Long brandId;
    private String name;
    private String slug;
    private boolean isActive;
}