package com.librarymanagementsystem.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivationRequest {

    @NotBlank(message = "Token is required")
    private String token;

    private String username;

    @Size(min = 6,message = "Password must be at least 6 characters")
    private String password;
}
