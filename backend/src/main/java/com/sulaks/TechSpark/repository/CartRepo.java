package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.enums.CartStatus;
import com.sulaks.TechSpark.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser_UserIdAndStatus(Long userId, CartStatus status);
}