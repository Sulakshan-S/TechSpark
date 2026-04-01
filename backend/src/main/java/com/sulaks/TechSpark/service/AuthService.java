package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.auth.AuthResponse;
import com.sulaks.TechSpark.dto.auth.LoginRequest;
import com.sulaks.TechSpark.dto.auth.RegisterRequest;
import com.sulaks.TechSpark.dto.auth.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser(String email);
}
