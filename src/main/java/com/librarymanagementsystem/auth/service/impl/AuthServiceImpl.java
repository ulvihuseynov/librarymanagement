package com.librarymanagementsystem.auth.service.impl;

import com.librarymanagementsystem.auth.dto.JwtResponse;
import com.librarymanagementsystem.auth.dto.LoginRequest;
import com.librarymanagementsystem.auth.dto.RegisterRequest;
import com.librarymanagementsystem.auth.service.AuthService;
import com.librarymanagementsystem.common.exception.DuplicateResourceException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.common.response.ApiResponse;
import com.librarymanagementsystem.email.service.EmailService;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberStatus;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.security.JwtUtils;
import com.librarymanagementsystem.security.UserDetailsImpl;
import com.librarymanagementsystem.user.entity.AppRole;
import com.librarymanagementsystem.user.entity.Role;
import com.librarymanagementsystem.user.entity.User;
import com.librarymanagementsystem.user.repository.RoleRepository;
import com.librarymanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Value("${app.activation.base-url}")
    private String baseUrl;

    @Override
    public ApiResponse<Object> login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();

        String token = jwtUtils.generateToken(userDetails);
        Set<String> roles = userDetails.getAuthorities().stream().map(
                GrantedAuthority::getAuthority).collect(Collectors.toSet());
        ;
        JwtResponse jwtResponse = new JwtResponse(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                token,
                "Bearer",
                roles
        );
        return ApiResponse.success("User successfully login", jwtResponse);
    }

    @Transactional
    @Override
    public ApiResponse<Object> register(RegisterRequest registerRequest) {

        boolean existsByUsername = userRepository.existsByUsername(registerRequest.getUsername());
        boolean existsByEmail = userRepository.existsByEmail(registerRequest.getEmail());
        boolean existsByMemberEmail = memberRepository.existsByEmail(registerRequest.getEmail());
        boolean existsByPhoneNumber = memberRepository.existsByPhoneNumber(registerRequest.getPhoneNumber());
        if (existsByUsername) {
            throw new DuplicateResourceException("Username already is exists " + registerRequest.getUsername());
        }

        if (existsByEmail) {
            throw new DuplicateResourceException("Email already is exists " + registerRequest.getEmail());
        }

        if (existsByMemberEmail) {
            throw new DuplicateResourceException("Member email already is exists " + registerRequest.getEmail());
        }

        if (existsByPhoneNumber) {
            throw new DuplicateResourceException("Phone number already is exists " + registerRequest.getPhoneNumber());
        }

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())

        );


        Role role = roleRepository.findByRoleName(AppRole.ROLE_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found " + AppRole.ROLE_MEMBER));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        Member member = new Member();
        member.setUser(user);
        member.setEmail(user.getEmail());
        member.setStatus(MemberStatus.ACTIVE);
        member.setAddress(registerRequest.getAddress());
        member.setFirstName(registerRequest.getFirstName());
        member.setLastName(registerRequest.getLastName());
        member.setPhoneNumber(registerRequest.getPhoneNumber());

        memberRepository.save(member);
        emailService.sendEmailVerificationEmail(member.getEmail(),baseUrl + "?token="+ user.getPassword(),user.getCreatedAt() );
        return ApiResponse.success("User and member profile successfully created ", null);
    }
}
