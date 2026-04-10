package com.sulaks.TechSpark.models;

import com.sulaks.TechSpark.enums.SpecDataType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_spec_attributes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category_id", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_spec_attribute_id")
    private Long productSpecAttributeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 30)
    private SpecDataType dataType = SpecDataType.TEXT;

    @Column(name = "is_required", nullable = false)
    private boolean required = false;

    @Column(name = "is_filterable", nullable = false)
    private boolean filterable = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}