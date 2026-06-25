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

    private final MemberActivationTokenRepository memberActivationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    @Value("${member.activation.token.duration-seconds}")
    private Long activationTokenS;

    public ActivationTokenResult createMemberActivationToken(Member member) {

        ActivationTokenResult activationTokenResult = new ActivationTokenResult();
        MemberActivationToken memberActivationToken = new MemberActivationToken();

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

        MemberActivationToken memberActivationToken = memberActivationTokenRepository.findByHashTokenAndExpiresAtGreaterThan(hashToken, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Activation token is invalid or expired"));

        if (memberActivationToken.getUsedAt() != null) {
            throw new BadRequestException("Activation token has already been used");
        }


        Member member = memberActivationToken.getMember();

        if (member.getUser() != null && !member.getUser().isEnabled()) {
            member.getUser().setEnabled(true);
        } else if (member.getUser() != null) {
            throw new BadRequestException("Account is already active");
        } else {

            if (activationRequest.getUsername() == null || activationRequest.getUsername().isBlank()) {
                throw new BadRequestException("Username is required");
            }

            if (activationRequest.getPassword() == null || (activationRequest.getPassword().length() < 6)
                    || activationRequest.getPassword().isBlank()) {
                throw new BadRequestException("Password is required or  must be at least 6 characters");
            }

            String username = activationRequest.getUsername().trim();
            boolean existsByUsername = userRepository.existsByUsername(username);
            boolean existsByEmail = userRepository.existsByEmail(member.getEmail());

            if (existsByUsername) {
                throw new DuplicateResourceException("Username already exists " + username);
            }

            if (existsByEmail) {
                throw new DuplicateResourceException("Email already exists " + member.getEmail());
            }

            Role role = roleRepository.findByRoleName(AppRole.ROLE_MEMBER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found " + AppRole.ROLE_MEMBER));
            User user = new User();


            user.setUsername(username);
            user.setEnabled(true);
            user.setRoles(Set.of(role));
            user.setEmail(member.getEmail());
            user.setPassword(passwordEncoder.encode(activationRequest.getPassword()));

            User savedUser = userRepository.save(user);
            member.setUser(savedUser);
        }

        memberActivationToken.setUsedAt(LocalDateTime.now());

        return "Account activated successfully";
    }
}
