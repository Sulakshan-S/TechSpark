package com.sulaks.TechSpark.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variant_attribute_values",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_variant_id", "variant_attribute_id"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class VariantAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_attribute_value_id")
    private Long variantAttributeValueId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "variant_attribute_id")
    private VariantAttribute variantAttribute;

    @Column(nullable = false, length = 120)
    private String value;
}
