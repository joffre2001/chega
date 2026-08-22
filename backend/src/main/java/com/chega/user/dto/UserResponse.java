package com.chega.user.dto;

import java.time.Instant;

import com.chega.user.SupportedLanguage;
import com.chega.user.User;
import com.chega.user.UserRole;
import com.chega.user.UserStatus;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        UserRole role,
        SupportedLanguage preferredLanguage,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getPreferredLanguage(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}