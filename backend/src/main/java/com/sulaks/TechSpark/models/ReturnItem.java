package com.sulaks.TechSpark.models;

import com.sulaks.TechSpark.enums.ConditionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "return_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_item_id")
    private Long returnItemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "return_id")
    private ReturnRequest returnRequest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status", nullable = false)
    private ConditionStatus conditionStatus = ConditionStatus.OPENED;
}