package com.chega.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.chega.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String base64Secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes
    ) {
        byte[] decodedKey = Decoders.BASE64.decode(base64Secret);

        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(User user) {
        Instant issuedAt = Instant.now();

        Instant expiresAt = issuedAt.plus(
                Duration.ofMinutes(expirationMinutes)
        );

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Claims claims = parseClaims(token);

            return claims.getSubject() != null
                    && claims.getExpiration() != null
                    && claims.getExpiration().after(new Date());
        }
        catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        String subject = parseClaims(token).getSubject();

        return Long.valueOf(subject);
    }

    public String extractEmail(String token) {
        return parseClaims(token).get(
                "email",
                String.class
        );
    }

    public String extractRole(String token) {
        return parseClaims(token).get(
                "role",
                String.class
        );
    }

    public long getExpirationSeconds() {
        return Duration
                .ofMinutes(expirationMinutes)
                .toSeconds();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}