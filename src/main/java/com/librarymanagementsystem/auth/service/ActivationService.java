package com.librarymanagementsystem.auth.service;

import com.librarymanagementsystem.member.entity.MemberActivationToken;
import com.librarymanagementsystem.member.repository.MemberActivationTokenRepository;
import com.librarymanagementsystem.security.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActivationService {

    @Value("${member.activation.token.duration-seconds}")
    private Long activationTokenS;

    private final MemberActivationTokenRepository memberActivationTokenRepository;
    private final TokenGenerator tokenGenerator;



    private   Date expiredDate(){

        return new Date(activationTokenS + System.currentTimeMillis());
    }

    private MemberActivationToken createEntity() throws NoSuchAlgorithmException {

        MemberActivationToken memberActivationToken=new MemberActivationToken();

        String rawToken = tokenGenerator.generateToken();
        String hashToken = tokenGenerator.hashToken(rawToken);

        memberActivationToken.setExpiresAt((expiredDate()));
        memberActivationToken.setTokenHash(hashToken);
        memberActivationToken.setUsedAt(null);

        return memberActivationTokenRepository.save(memberActivationToken);
    }
}
