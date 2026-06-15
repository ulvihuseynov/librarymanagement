package com.librarymanagementsystem.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private Long userId;
    private String username;
    private String email;
    private String token;
    private String type;
    private Set<String> roles;
}
