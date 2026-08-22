package com.chega.user.dto;

import com.chega.user.SupportedLanguage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(
                min = 3,
                max = 120,
                message = "O nome deve ter entre 3 e 120 caracteres."
        )
        String fullName,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(
                max = 180,
                message = "O e-mail deve ter no máximo 180 caracteres."
        )
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(
                min = 8,
                max = 72,
                message = "A senha deve ter entre 8 e 72 caracteres."
        )
        String password,

        @NotNull(message = "O idioma preferido é obrigatório.")
        SupportedLanguage preferredLanguage

) {
}