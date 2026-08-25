package com.chega.profile;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chega.profile.dto.CreateMigrantProfileRequest;
import com.chega.profile.dto.MigrantProfileResponse;
import com.chega.profile.dto.UpdateMigrantProfileRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
public class MigrantProfileController {

    private final MigrantProfileService profileService;

    public MigrantProfileController(
            MigrantProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MigrantProfileResponse create(
            Authentication authentication,
            @Valid @RequestBody CreateMigrantProfileRequest request) {
        return profileService.create(
                authentication.getName(),
                request);
    }

    @GetMapping
    public MigrantProfileResponse findCurrent(
            Authentication authentication) {
        return profileService.findCurrent(
                authentication.getName());
    }

    @PutMapping
    public MigrantProfileResponse update(
            Authentication authentication,
            @Valid @RequestBody UpdateMigrantProfileRequest request) {
        return profileService.update(
                authentication.getName(),
                request);
    }
}