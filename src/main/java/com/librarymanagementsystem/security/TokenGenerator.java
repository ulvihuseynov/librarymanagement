package com.librarymanagementsystem.security;

import com.librarymanagementsystem.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public  class TokenGenerator {

    private final SecureRandom secureRandom;

    public String generateToken(){

        byte [] nextBytes=new byte[32];

        secureRandom.nextBytes(nextBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(nextBytes);
    }

    public String hashToken(String rawToken)   {
       try{
           MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

           byte[] hashBytes = messageDigest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
           return Base64.getUrlEncoder().encodeToString(hashBytes);

       }catch (GeneralSecurityException e){

           throw new BadRequestException(e.getMessage());
       }

    }
}
