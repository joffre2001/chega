package com.chega.profile;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chega.exception.ConsentRequiredException;
import com.chega.exception.InvalidCredentialsException;
import com.chega.exception.MigrantProfileAlreadyExistsException;
import com.chega.exception.MigrantProfileNotFoundException;
import com.chega.profile.dto.CreateMigrantProfileRequest;
import com.chega.profile.dto.MigrantProfileResponse;
import com.chega.profile.dto.UpdateMigrantProfileRequest;
import com.chega.user.User;
import com.chega.user.UserRepository;

@Service
public class MigrantProfileService {

    private final MigrantProfileRepository profileRepository;
    private final UserRepository userRepository;

    public MigrantProfileService(
            MigrantProfileRepository profileRepository,
            UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MigrantProfileResponse create(
            String authenticatedEmail,
            CreateMigrantProfileRequest request) {
        if (!Boolean.TRUE.equals(request.consent())) {
            throw new ConsentRequiredException();
        }

        User user = userRepository
                .findByEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(InvalidCredentialsException::new);

        if (profileRepository.existsByUserId(user.getId())) {
            throw new MigrantProfileAlreadyExistsException();
        }

        MigrantProfile profile = new MigrantProfile(
                user,
                normalizeText(request.nationality()),
                normalizeText(request.currentCity()),
                request.arrivalDate(),
                request.migrationSituation(),
                request.primaryGoal(),
                Instant.now());

        MigrantProfile savedProfile = profileRepository.save(profile);

        return MigrantProfileResponse.from(savedProfile);
    }

    private String normalizeText(String text) {
        return text
                .trim()
                .replaceAll("\\s+", " ");
    }

    public MigrantProfileResponse findCurrent(
            String authenticatedEmail) {
        MigrantProfile profile = profileRepository
                .findByUserEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(MigrantProfileNotFoundException::new);

        return MigrantProfileResponse.from(profile);
    }

    @Transactional
    public MigrantProfileResponse update(
            String authenticatedEmail,
            UpdateMigrantProfileRequest request) {
        MigrantProfile profile = profileRepository
                .findByUserEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(MigrantProfileNotFoundException::new);

        profile.update(
                normalizeText(request.nationality()),
                normalizeText(request.currentCity()),
                request.arrivalDate(),
                request.migrationSituation(),
                request.primaryGoal());

        MigrantProfile updatedProfile = profileRepository.save(profile);

        return MigrantProfileResponse.from(updatedProfile);
    }
}