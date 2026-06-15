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
public class LoginRequest {


    @NotBlank(message = "Username is required")
    private String username;


    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be 6 and 100 characters")
    private String password;
}
