package com.librarymanagementsystem.common.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library Management API",
                version = "1.0.0",
                description = """
                        REST API for managing books, members, loans,
                        reservations, fines and authentication.
                        """,
                contact = @Contact(
                        name = "Library Management Team",
                        email = "librarymanagement.az@gmail.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local development server"
                )
        }
)
@SecurityScheme(
        name = OpenApiConfig.BEARER_AUTH,
        description = "JWT authentication. Enter only the token, without the Bearer prefix.",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";
}