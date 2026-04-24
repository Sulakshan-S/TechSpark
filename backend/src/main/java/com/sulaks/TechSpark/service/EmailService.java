package com.sulaks.TechSpark.service;


public interface EmailService {
    void sendVerificationOtp(String toEmail, String otp);
}