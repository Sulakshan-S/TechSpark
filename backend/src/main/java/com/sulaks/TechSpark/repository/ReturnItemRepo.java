package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.ReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnItemRepo extends JpaRepository<ReturnItem, Long> {

    List<ReturnItem> findByReturnRequest_ReturnId(Long returnId);

    List<ReturnItem> findByOrderItem_OrderItemId(Long orderItemId);
}