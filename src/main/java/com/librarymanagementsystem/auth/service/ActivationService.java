package com.librarymanagementsystem.auth.service;

import com.librarymanagementsystem.auth.dto.ActivationRequest;
import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.DuplicateResourceException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.member.dto.ActivationTokenResult;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberActivationToken;
import com.librarymanagementsystem.member.repository.MemberActivationTokenRepository;
import com.librarymanagementsystem.security.TokenGenerator;
import com.librarymanagementsystem.user.entity.AppRole;
import com.librarymanagementsystem.user.entity.Role;
import com.librarymanagementsystem.user.entity.User;
import com.librarymanagementsystem.user.repository.RoleRepository;
import com.librarymanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActivationService {

    @Value("${member.activation.token.duration-seconds}")
    private Long activationTokenS;

    private final MemberActivationTokenRepository memberActivationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;



    public ActivationTokenResult createMemberActivationToken(Member member)  {

        ActivationTokenResult activationTokenResult=new ActivationTokenResult();
        MemberActivationToken memberActivationToken=new MemberActivationToken();

        String rawToken = tokenGenerator.generateToken();
        String hashToken = tokenGenerator.hashToken(rawToken);

        memberActivationToken.setExpiresAt(LocalDateTime.now().plusSeconds(activationTokenS));
        memberActivationToken.setHashToken(hashToken);
        memberActivationToken.setUsedAt(null);
        memberActivationToken.setMember(member);

        memberActivationTokenRepository.save(memberActivationToken);
        activationTokenResult.setRawToken(rawToken);
        activationTokenResult.setExpiryDate(memberActivationToken.getExpiresAt());

        return activationTokenResult;
    }

    @Transactional
    public String activate(ActivationRequest activationRequest) {

        String hashToken = tokenGenerator.hashToken(activationRequest.getToken());

        MemberActivationToken memberActivationToken=memberActivationTokenRepository.findByHashToken(hashToken)
                .orElseThrow(()->new ResourceNotFoundException("Activation token is invalid"));

        if (memberActivationToken.getUsedAt()!=null){
            throw new BadRequestException("Token already is used");
        }

        LocalDateTime expiresAt = memberActivationToken.getExpiresAt();

        if (expiresAt.isEqual(LocalDateTime.now())){
            throw new BadRequestException("Token expiration date has expired. ");
        }

        Member member = memberActivationToken.getMember();

        if (member.getUser() !=null){
            throw new DuplicateResourceException("User already exist");
        }

        Role role = roleRepository.findByRoleName(AppRole.ROLE_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found " + AppRole.ROLE_MEMBER));

        boolean existsByUsername = userRepository.existsByUsername(activationRequest.getUsername());
        boolean existsByEmail = userRepository.existsByEmail(member.getEmail());
        if (existsByUsername){
            throw new DuplicateResourceException("Username already exists "+activationRequest.getUsername());
        }

        if (existsByEmail){
            throw new DuplicateResourceException("Email already exists " +member.getEmail());
        }

        User user =new User();

        user.setUsername(activationRequest.getUsername());
        user.setEnabled(true);
        user.setRoles(Set.of(role));
        user.setEmail(member.getEmail());
        user.setPassword(passwordEncoder.encode(activationRequest.getPassword()));

        User savedUser = userRepository.save(user);
        member.setUser(savedUser);
        memberActivationToken.setUsedAt(LocalDateTime.now());

        return "Account activated successfully";
    }
}
