package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.EmailVerificationToken;
import com.sulaks.TechSpark.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepo extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findTopByUserOrderByCreatedAtDesc(User user);
}