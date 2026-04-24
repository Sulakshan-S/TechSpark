package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.auth.*;


public interface AuthService {
    MessageResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser(String email);
    MessageResponse verifyOtp(VerifyOtpRequest request);
    MessageResponse resendOtp(ResendOtpRequest request);
}
