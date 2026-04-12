package com.sulaks.TechSpark.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variant_attributes",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class VariantAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_attribute_id")
    private Long variantAttributeId;

    @Column(nullable = false, length = 80)
    private String name;
}