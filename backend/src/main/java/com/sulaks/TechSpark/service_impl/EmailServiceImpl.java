package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your TechSpark verification OTP");
        message.setText("Your OTP is: " + otp + "\nIt will expire in 10 minutes.");

        mailSender.send(message);
    }
}