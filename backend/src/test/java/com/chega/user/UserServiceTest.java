package com.chega.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chega.exception.EmailAlreadyRegisteredException;
import com.chega.user.dto.CreateUserRequest;
import com.chega.user.dto.UserResponse;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithNormalizedDataAndHashedPassword() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "  Usuario    Teste  ",
                "  TESTE@CHEGA.COM  ",
                "Chega@2026",
                SupportedLanguage.PT_BR
        );

        when(userRepository.existsByEmailIgnoreCase(
                "teste@chega.com"
        )).thenReturn(false);

        when(passwordEncoder.encode("Chega@2026"))
                .thenReturn("hash-bcrypt-gerado");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        // Act
        UserResponse response = userService.create(request);

        // Assert
        assertEquals("Usuario Teste", response.fullName());
        assertEquals("teste@chega.com", response.email());
        assertEquals(UserRole.USER, response.role());
        assertEquals(UserStatus.ACTIVE, response.status());

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(
                "hash-bcrypt-gerado",
                savedUser.getPasswordHash()
        );

        verify(passwordEncoder).encode("Chega@2026");
    }

    @Test
    void shouldRejectUserWhenEmailIsAlreadyRegistered() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Usuario Teste",
                "TESTE@CHEGA.COM",
                "Chega@2026",
                SupportedLanguage.PT_BR
        );

        when(userRepository.existsByEmailIgnoreCase(
                "teste@chega.com"
        )).thenReturn(true);

        // Act + Assert
        assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> userService.create(request)
        );

        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never())
                .save(any(User.class));
    }
}