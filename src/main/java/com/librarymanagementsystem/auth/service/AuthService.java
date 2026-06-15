package com.librarymanagementsystem.auth.service;

import com.librarymanagementsystem.auth.dto.LoginRequest;
import com.librarymanagementsystem.auth.dto.RegisterRequest;
import com.librarymanagementsystem.common.response.ApiResponse;
import jakarta.validation.Valid;

public interface AuthService {
    ApiResponse<Object> login(@Valid LoginRequest loginRequest);

    ApiResponse<Object> register(@Valid RegisterRequest registerRequest);
}
