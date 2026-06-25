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
    public void sendAccountSetupEmail(String recipientEmail, String activationLink, LocalDateTime expiresAt) {


        String subject = "Set up your Library Management account";

        String body = """
        Hello,

        Your library membership account has been created.

        Complete your account registration using the following link:

        %s

        This link expires at: %s

        If you did not expect this email, you can ignore it.
        """.formatted(activationLink, expiresAt);

        SimpleMailMessage message = sendSimpleEmail(recipientEmail, subject, body);


        javaMailSender.send(message);

    }
    @Override
    public void sendEmailVerificationEmail(String recipientEmail, String verificationLink, LocalDateTime expiresAt) {


        String subject = "Verify your Library Management email";

        String body = """
        Hello,

        Your Library Management account has been created.

        Verify your email address using the following link:

        %s

        This link expires at: %s

        If you did not create this account, you can ignore this email.
        """.formatted(verificationLink, expiresAt);


        SimpleMailMessage message = sendSimpleEmail(recipientEmail, subject, body);


        javaMailSender.send(message);

    }


    private SimpleMailMessage sendSimpleEmail(String recipientEmail, String subject, String body) {

        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(recipientEmail);
        message.setFrom(senderEmail);
        message.setSubject(subject);
        message.setText(body);

        return message;
    }


}
