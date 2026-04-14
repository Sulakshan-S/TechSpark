package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepo extends JpaRepository<WishlistItem, Long> {

    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    List<WishlistItem> findByUser_UserId(Long userId);

    Optional<WishlistItem> findByWishlistItemIdAndUser_UserId(Long wishlistItemId, Long userId);

    Optional<WishlistItem> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
}