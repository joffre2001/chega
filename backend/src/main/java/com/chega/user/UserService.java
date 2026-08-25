package com.chega.user;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chega.exception.EmailAlreadyRegisteredException;
import com.chega.exception.InvalidCredentialsException;
import com.chega.user.dto.CreateUserRequest;
import com.chega.user.dto.UserResponse;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        String normalizedName = normalizeName(request.fullName());
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException();
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                normalizedName,
                normalizedEmail,
                passwordHash,
                request.preferredLanguage());

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);

        return UserResponse.from(user);
    }

    private String normalizeName(String name) {
        return name
                .trim()
                .replaceAll("\\s+", " ");
    }

    private String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}