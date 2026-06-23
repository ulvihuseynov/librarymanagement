package com.librarymanagementsystem.auth.controller;

import com.librarymanagementsystem.auth.dto.ActivationRequest;
import com.librarymanagementsystem.auth.dto.LoginRequest;
import com.librarymanagementsystem.auth.dto.RegisterRequest;
import com.librarymanagementsystem.auth.service.ActivationService;
import com.librarymanagementsystem.auth.service.AuthService;
import com.librarymanagementsystem.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ActivationService activationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest loginRequest) {

        ApiResponse<Object> loginResponse = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {

        ApiResponse<Object> registerResponse = authService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse<String>> activationMember(@Valid @RequestBody ActivationRequest activationRequest) {

        String status = activationService.activate(activationRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(status, null));
    }
}
