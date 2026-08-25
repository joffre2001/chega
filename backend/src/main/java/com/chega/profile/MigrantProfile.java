package com.chega.profile;

import java.time.Instant;
import java.time.LocalDate;

import com.chega.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "migrant_profiles")
public class MigrantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String nationality;

    @Column(name = "current_city", nullable = false, length = 120)
    private String currentCity;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "migration_situation", nullable = false, length = 40)
    private MigrationSituation migrationSituation;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_goal", nullable = false, length = 30)
    private PrimaryGoal primaryGoal;

    @Column(name = "consent_given_at", nullable = false, updatable = false)
    private Instant consentGivenAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected MigrantProfile() {
    }

    public MigrantProfile(
            User user,
            String nationality,
            String currentCity,
            LocalDate arrivalDate,
            MigrationSituation migrationSituation,
            PrimaryGoal primaryGoal,
            Instant consentGivenAt) {
        this.user = user;
        this.nationality = nationality;
        this.currentCity = currentCity;
        this.arrivalDate = arrivalDate;
        this.migrationSituation = migrationSituation;
        this.primaryGoal = primaryGoal;
        this.consentGivenAt = consentGivenAt;
    }

    @PrePersist
    private void beforeInsert() {
        Instant now = Instant.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public MigrationSituation getMigrationSituation() {
        return migrationSituation;
    }

    public void setMigrationSituation(
            MigrationSituation migrationSituation) {
        this.migrationSituation = migrationSituation;
    }

    public PrimaryGoal getPrimaryGoal() {
        return primaryGoal;
    }

    public void setPrimaryGoal(PrimaryGoal primaryGoal) {
        this.primaryGoal = primaryGoal;
    }

    public Instant getConsentGivenAt() {
        return consentGivenAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String nationality,
            String currentCity,
            LocalDate arrivalDate,
            MigrationSituation migrationSituation,
            PrimaryGoal primaryGoal) {
        this.nationality = nationality;
        this.currentCity = currentCity;
        this.arrivalDate = arrivalDate;
        this.migrationSituation = migrationSituation;
        this.primaryGoal = primaryGoal;
    }
}