package com.sulaks.TechSpark.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_spec_values",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "product_spec_attribute_id"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductSpecValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_spec_value_id")
    private Long productSpecValueId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_spec_attribute_id")
    private ProductSpecAttribute productSpecAttribute;

    @Column(nullable = false, length = 180)
    private String value;
}