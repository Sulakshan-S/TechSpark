package com.sulaks.TechSpark.models;

import com.sulaks.TechSpark.enums.SpecDataType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_spec_attributes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductSpecAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_spec_attribute_id")
    private Long productSpecAttributeId;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private SpecDataType dataType = SpecDataType.TEXT;
}
