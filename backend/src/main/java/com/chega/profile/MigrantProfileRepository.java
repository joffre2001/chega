package com.chega.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MigrantProfileRepository
        extends JpaRepository<MigrantProfile, Long> {

    Optional<MigrantProfile> findByUserId(Long userId);

    Optional<MigrantProfile> findByUserEmailIgnoreCase(
            String email
    );

    boolean existsByUserId(Long userId);
}