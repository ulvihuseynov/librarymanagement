package com.librarymanagementsystem.auth.service;

import com.librarymanagementsystem.member.dto.ActivationTokenResult;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberActivationToken;
import com.librarymanagementsystem.member.repository.MemberActivationTokenRepository;
import com.librarymanagementsystem.security.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationService {

    @Value("${member.activation.token.duration-seconds}")
    private Long activationTokenS;

    private final MemberActivationTokenRepository memberActivationTokenRepository;
    private final TokenGenerator tokenGenerator;



    public ActivationTokenResult createMemberActivationToken(Member member)  {

        ActivationTokenResult activationTokenResult=new ActivationTokenResult();
        MemberActivationToken memberActivationToken=new MemberActivationToken();

        String rawToken = tokenGenerator.generateToken();
        String hashToken = tokenGenerator.hashToken(rawToken);

        memberActivationToken.setExpiresAt(LocalDateTime.now().plusSeconds(activationTokenS));
        memberActivationToken.setTokenHash(hashToken);
        memberActivationToken.setUsedAt(null);
        memberActivationToken.setMember(member);

        memberActivationTokenRepository.save(memberActivationToken);
        activationTokenResult.setRawToken(rawToken);
        activationTokenResult.setExpiryDate(memberActivationToken.getExpiresAt());

        return activationTokenResult;
    }
}
