package com.librarymanagementsystem.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public  class TokenGenerator {

    byte [] nextBytes=new byte[32];
    public String generateToken(){
        SecureRandom secureRandom=new SecureRandom();
        secureRandom.nextBytes(nextBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(nextBytes);
    }

    public String hashToken(String rawToken) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] hashBytes = messageDigest.digest(rawToken.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder().encodeToString(hashBytes);
    }
}
