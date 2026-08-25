package com.chega.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.chega.user.User;
import com.chega.user.UserRepository;
import com.chega.user.UserRole;
import com.chega.user.UserStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtAuthenticationFilterTest {

    private final JwtService jwtService =
            mock(JwtService.class);

    private final UserRepository userRepository =
            mock(UserRepository.class);

    private final JwtAuthenticationFilter filter =
            new JwtAuthenticationFilter(
                    jwtService,
                    userRepository
            );

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueWithoutAuthenticationWhenHeaderIsMissing()
            throws Exception {
        // Arrange
        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        FilterChain filterChain =
                mock(FilterChain.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(null);

        // Act
        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        // Assert
        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldContinueWithoutAuthenticationWhenTokenIsInvalid()
            throws Exception {
        // Arrange
        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        FilterChain filterChain =
                mock(FilterChain.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn("Bearer token-invalido");

        when(jwtService.isValid("token-invalido"))
                .thenReturn(false);

        // Act
        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        // Assert
        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldAuthenticateActiveUserWhenTokenIsValid()
            throws Exception {
        // Arrange
        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        FilterChain filterChain =
                mock(FilterChain.class);

        User user = mock(User.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn("Bearer jwt-valido");

        when(jwtService.isValid("jwt-valido"))
                .thenReturn(true);

        when(jwtService.extractUserId("jwt-valido"))
                .thenReturn(42L);

        when(userRepository.findById(42L))
                .thenReturn(Optional.of(user));

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(user.getEmail())
                .thenReturn("teste@chega.com");

        when(user.getRole())
                .thenReturn(UserRole.USER);

        // Act
        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        // Assert
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertNotNull(authentication);
        assertEquals(
                "teste@chega.com",
                authentication.getName()
        );
        assertEquals(
                "ROLE_USER",
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
        );

        verify(filterChain).doFilter(request, response);
    }
}