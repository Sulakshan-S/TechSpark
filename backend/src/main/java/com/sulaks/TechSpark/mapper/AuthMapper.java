package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.auth.AuthResponse;
import com.sulaks.TechSpark.dto.auth.RegisterRequest;
import com.sulaks.TechSpark.dto.auth.UserResponse;
import com.sulaks.TechSpark.enums.UserRole;
import com.sulaks.TechSpark.enums.UserStatus;
import com.sulaks.TechSpark.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toUser(RegisterRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(UserRole.CUSTOMER)
                .status(UserStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .build();
    }

    public AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}