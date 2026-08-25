package com.chega.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chega.auth.dto.LoginRequest;
import com.chega.auth.dto.LoginResponse;
import com.chega.exception.InvalidCredentialsException;
import com.chega.security.JwtService;
import com.chega.user.SupportedLanguage;
import com.chega.user.User;
import com.chega.user.UserRepository;
import com.chega.user.UserStatus;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtService jwtService;

        @InjectMocks
        private AuthService authService;

        @Test
        void shouldAuthenticateActiveUserWithValidCredentials() {
                // Arrange
                User user = new User(
                                "Usuario Teste",
                                "teste@chega.com",
                                "hash-bcrypt-salvo",
                                SupportedLanguage.PT_BR);

                LoginRequest request = new LoginRequest(
                                "TESTE@CHEGA.COM",
                                "Chega@2026");

                when(userRepository.findByEmailIgnoreCase("teste@chega.com"))
                                .thenReturn(Optional.of(user));

                when(passwordEncoder.matches(
                                "Chega@2026",
                                "hash-bcrypt-salvo")).thenReturn(true);

                when(jwtService.generateToken(user))
                                .thenReturn("jwt-de-teste");

                when(jwtService.getExpirationSeconds())
                                .thenReturn(3600L);

                // Act
                LoginResponse response = authService.authenticate(request);

                // Assert
                assertEquals("jwt-de-teste", response.accessToken());
                assertEquals("Bearer", response.tokenType());
                assertEquals(3600L, response.expiresIn());
                assertEquals("teste@chega.com", response.user().email());

                verify(userRepository)
                                .findByEmailIgnoreCase("teste@chega.com");

                verify(passwordEncoder)
                                .matches("Chega@2026", "hash-bcrypt-salvo");

                verify(jwtService)
                                .generateToken(user);
        }

        @Test
        void shouldRejectLoginWhenEmailDoesNotExist() {
                // Arrange
                LoginRequest request = new LoginRequest(
                                "inexistente@chega.com",
                                "Chega@2026");

                when(userRepository.findByEmailIgnoreCase(
                                "inexistente@chega.com")).thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(
                                InvalidCredentialsException.class,
                                () -> authService.authenticate(request));

                verifyNoInteractions(passwordEncoder);
                verifyNoInteractions(jwtService);
        }

        @Test
        void shouldRejectLoginWhenPasswordIsInvalid() {
                // Arrange
                User user = new User(
                                "Usuario Teste",
                                "teste@chega.com",
                                "hash-bcrypt-salvo",
                                SupportedLanguage.PT_BR);

                LoginRequest request = new LoginRequest(
                                "teste@chega.com",
                                "SenhaIncorreta@2026");

                when(userRepository.findByEmailIgnoreCase(
                                "teste@chega.com")).thenReturn(Optional.of(user));

                when(passwordEncoder.matches(
                                "SenhaIncorreta@2026",
                                "hash-bcrypt-salvo")).thenReturn(false);

                // Act + Assert
                assertThrows(
                                InvalidCredentialsException.class,
                                () -> authService.authenticate(request));

                verifyNoInteractions(jwtService);
        }

        @Test
        void shouldRejectLoginWhenUserIsBlocked() {
                // Arrange
                User user = new User(
                                "Usuario Bloqueado",
                                "bloqueado@chega.com",
                                "hash-bcrypt-salvo",
                                SupportedLanguage.PT_BR);

                user.setStatus(UserStatus.BLOCKED);

                LoginRequest request = new LoginRequest(
                                "bloqueado@chega.com",
                                "Chega@2026");

                when(userRepository.findByEmailIgnoreCase(
                                "bloqueado@chega.com")).thenReturn(Optional.of(user));

                when(passwordEncoder.matches(
                                "Chega@2026",
                                "hash-bcrypt-salvo")).thenReturn(true);

                // Act + Assert
                assertThrows(
                                InvalidCredentialsException.class,
                                () -> authService.authenticate(request));

                verifyNoInteractions(jwtService);
        }
}