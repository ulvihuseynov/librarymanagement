package com.librarymanagementsystem.email.service;

import java.time.LocalDateTime;

public interface EmailService {

    void sendActivationEmail(
            String recipientEmail,
            String activationLink,
            LocalDateTime expiresAt
    );
}
