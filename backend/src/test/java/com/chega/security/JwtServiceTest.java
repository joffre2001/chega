package com.chega.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.chega.user.User;
import com.chega.user.UserRole;

class JwtServiceTest {

    private static final String TEST_SECRET =
            "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    @Test
    void shouldGenerateAndReadValidToken() {
        // Arrange
        JwtService jwtService = new JwtService(
                TEST_SECRET,
                60
        );

        User user = mock(User.class);

        when(user.getId()).thenReturn(42L);
        when(user.getEmail()).thenReturn("teste@chega.com");
        when(user.getRole()).thenReturn(UserRole.USER);

        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertTrue(jwtService.isValid(token));
        assertEquals(42L, jwtService.extractUserId(token));
        assertEquals(
                "teste@chega.com",
                jwtService.extractEmail(token)
        );
        assertEquals(
                "USER",
                jwtService.extractRole(token)
        );
        assertEquals(3600L, jwtService.getExpirationSeconds());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void shouldRejectTamperedToken() {
        // Arrange
        JwtService jwtService = new JwtService(
                TEST_SECRET,
                60
        );

        User user = mock(User.class);

        when(user.getId()).thenReturn(42L);
        when(user.getEmail()).thenReturn("teste@chega.com");
        when(user.getRole()).thenReturn(UserRole.USER);

        String token = jwtService.generateToken(user);

        String finalCharacter = token.endsWith("a")
                ? "b"
                : "a";

        String tamperedToken =
                token.substring(0, token.length() - 1)
                + finalCharacter;

        // Act
        boolean valid = jwtService.isValid(tamperedToken);

        // Assert
        assertFalse(valid);
    }

    @Test
    void shouldRejectExpiredToken() {
        // Arrange
        JwtService jwtService = new JwtService(
                TEST_SECRET,
                -1
        );

        User user = mock(User.class);

        when(user.getId()).thenReturn(42L);
        when(user.getEmail()).thenReturn("teste@chega.com");
        when(user.getRole()).thenReturn(UserRole.USER);

        // Act
        String expiredToken = jwtService.generateToken(user);

        // Assert
        assertFalse(jwtService.isValid(expiredToken));
    }
}