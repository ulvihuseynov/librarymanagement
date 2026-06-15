package com.librarymanagementsystem.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String  secretKey;

    @Value("${app.jwt.expiration-ms}")
    private long  expirationMs;

    public String generateToken(UserDetails userDetails) {


        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignInKey())
                .compact();

    }

    private SecretKey getSignInKey() {
      return   Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String getUsernameFromToken(String token) {


        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();

    }

    public boolean jwtTokenValidation(String token) {

        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException
                 | SignatureException | IllegalArgumentException e) {
            logger.error("Jwt validation error {}", e.getMessage());
        }

        return false;
    }
}
