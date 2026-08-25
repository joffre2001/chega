package com.chega.profile.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.chega.profile.MigrantProfile;
import com.chega.profile.MigrationSituation;
import com.chega.profile.PrimaryGoal;

public record MigrantProfileResponse(

        Long id,
        Long userId,
        String nationality,
        String currentCity,
        LocalDate arrivalDate,
        MigrationSituation migrationSituation,
        PrimaryGoal primaryGoal,
        Instant consentGivenAt,
        Instant createdAt,
        Instant updatedAt

) {

    public static MigrantProfileResponse from(
            MigrantProfile profile
    ) {
        return new MigrantProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getNationality(),
                profile.getCurrentCity(),
                profile.getArrivalDate(),
                profile.getMigrationSituation(),
                profile.getPrimaryGoal(),
                profile.getConsentGivenAt(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}