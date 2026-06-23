package com.librarymanagementsystem.email.impl;

import com.librarymanagementsystem.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;
    @Override
    public void sendActivationEmail(String recipientEmail, String activationLink, LocalDateTime expiresAt) {

        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(recipientEmail);
        message.setFrom(senderEmail);
        message.setSubject("Activate your Library Management account");
        message.setText("Hello,\n" +
                "\n" +
                "Your library membership account has been created.\n" +
                "\n" +
                "Complete your account registration using the following link:\n" +
                "\n" +
                "ACTIVATION_LINK\n" + activationLink +
                "\n" +
                "This link expires at: EXPIRES_AT\n" + expiresAt +
                "\n" +
                "If you did not expect this email, you can ignore it.");

        javaMailSender.send(message);

    }
}
