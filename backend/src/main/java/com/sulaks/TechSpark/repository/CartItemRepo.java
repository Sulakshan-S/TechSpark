package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart_CartId(Long cartId);

    Optional<CartItem> findByCart_CartIdAndProductVariant_ProductVariantId(Long cartId, Long productVariantId);

    Optional<CartItem> findByCartItemIdAndCart_CartId(Long cartItemId, Long cartId);

    void deleteByCart_CartId(Long cartId);
}