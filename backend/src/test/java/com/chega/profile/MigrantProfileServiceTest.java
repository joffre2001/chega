package com.chega.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chega.exception.MigrantProfileNotFoundException;
import com.chega.profile.dto.MigrantProfileResponse;
import com.chega.profile.dto.UpdateMigrantProfileRequest;
import com.chega.user.User;
import com.chega.user.UserRepository;

class MigrantProfileServiceTest {

    private MigrantProfileRepository profileRepository;
    private UserRepository userRepository;
    private MigrantProfileService profileService;

    @BeforeEach
    void setUp() {
        profileRepository = mock(MigrantProfileRepository.class);
        userRepository = mock(UserRepository.class);

        profileService = new MigrantProfileService(
                profileRepository,
                userRepository);
    }

    @Test
    void shouldUpdateCurrentUserProfile() {
        String email = "teste@chega.com";
        LocalDate arrivalDate = LocalDate.of(2024, 1, 15);
        Instant now = Instant.now();

        User user = mock(User.class);
        MigrantProfile profile = mock(MigrantProfile.class);

        UpdateMigrantProfileRequest request = new UpdateMigrantProfileRequest(
                "Haitiana",
                "Chapeco - SC",
                arrivalDate,
                MigrationSituation.RESIDENCE_PERMIT,
                PrimaryGoal.WORK);

        when(user.getId()).thenReturn(1L);

        when(profile.getId()).thenReturn(1L);
        when(profile.getUser()).thenReturn(user);
        when(profile.getNationality()).thenReturn("Haitiana");
        when(profile.getCurrentCity()).thenReturn("Chapeco - SC");
        when(profile.getArrivalDate()).thenReturn(arrivalDate);
        when(profile.getMigrationSituation())
                .thenReturn(MigrationSituation.RESIDENCE_PERMIT);
        when(profile.getPrimaryGoal()).thenReturn(PrimaryGoal.WORK);
        when(profile.getConsentGivenAt()).thenReturn(now);
        when(profile.getCreatedAt()).thenReturn(now);
        when(profile.getUpdatedAt()).thenReturn(now);

        when(profileRepository.findByUserEmailIgnoreCase(email))
                .thenReturn(Optional.of(profile));

        when(profileRepository.save(profile))
                .thenReturn(profile);

        MigrantProfileResponse response = profileService.update(email, request);

        assertEquals("Chapeco - SC", response.currentCity());
        assertEquals(
                MigrationSituation.RESIDENCE_PERMIT,
                response.migrationSituation());
        assertEquals(PrimaryGoal.WORK, response.primaryGoal());

        verify(profile).update(
                "Haitiana",
                "Chapeco - SC",
                arrivalDate,
                MigrationSituation.RESIDENCE_PERMIT,
                PrimaryGoal.WORK);

        verify(profileRepository).save(profile);
    }

    @Test
    void shouldThrowExceptionWhenProfileDoesNotExist() {
        String email = "semperfil@chega.com";

        UpdateMigrantProfileRequest request = new UpdateMigrantProfileRequest(
                "Haitiana",
                "Chapeco - SC",
                LocalDate.of(2024, 1, 15),
                MigrationSituation.RESIDENCE_PERMIT,
                PrimaryGoal.WORK);

        when(profileRepository.findByUserEmailIgnoreCase(email))
                .thenReturn(Optional.empty());

        assertThrows(
                MigrantProfileNotFoundException.class,
                () -> profileService.update(email, request));

        verify(profileRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }
}