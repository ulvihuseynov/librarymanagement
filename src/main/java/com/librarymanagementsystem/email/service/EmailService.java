package com.librarymanagementsystem.email.service;

import java.time.LocalDateTime;

public interface EmailService {

    void sendAccountSetupEmail(
            String recipientEmail,
            String activationLink,
            LocalDateTime expiresAt
    );

     void sendEmailVerificationEmail(String recipientEmail,
                                     String verificationLink,
                                     LocalDateTime expiresAt);

}
