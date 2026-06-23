package com.librarymanagementsystem.security;

import com.librarymanagementsystem.common.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public  class TokenGenerator {

    public String generateToken(){

        byte [] nextBytes=new byte[32];

        SecureRandom secureRandom=new SecureRandom();
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
