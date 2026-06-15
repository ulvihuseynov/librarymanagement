package com.librarymanagementsystem.auth.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Username is required")
    @Email(message = "Please enter the correct email format")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 6,max = 12,message = "Password must be 6 and 12 characters")
    private String password;
}
