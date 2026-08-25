package com.chega.profile.dto;

import java.time.LocalDate;

import com.chega.profile.MigrationSituation;
import com.chega.profile.PrimaryGoal;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record CreateMigrantProfileRequest(

        @NotBlank(message = "A nacionalidade é obrigatória.")
        @Size(
                min = 2,
                max = 100,
                message = "A nacionalidade deve ter entre 2 e 100 caracteres."
        )
        String nationality,

        @NotBlank(message = "A cidade atual é obrigatória.")
        @Size(
                min = 2,
                max = 120,
                message = "A cidade deve ter entre 2 e 120 caracteres."
        )
        String currentCity,

        @PastOrPresent(
                message = "A data de chegada não pode estar no futuro."
        )
        LocalDate arrivalDate,

        @NotNull(message = "A situação migratória é obrigatória.")
        MigrationSituation migrationSituation,

        @NotNull(message = "O objetivo principal é obrigatório.")
        PrimaryGoal primaryGoal,

        @NotNull(message = "O consentimento deve ser informado.")
        @AssertTrue(
                message = "É necessário consentir com o tratamento dos dados."
        )
        Boolean consent

) {
}