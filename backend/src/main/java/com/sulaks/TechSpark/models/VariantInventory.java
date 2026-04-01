package com.sulaks.TechSpark.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "variant_inventory")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class VariantInventory {

    @Id
    @Column(name = "product_variant_id")
    private Long productVariantId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 0;

    @Column(name = "reserved_qty", nullable = false)
    private Integer reservedQty = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}