package com.chega.document;

import java.time.Instant;

import com.chega.profile.MigrationSituation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_requirements")
public class DocumentRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 80
    )
    private String code;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "official_source_url", length = 500)
    private String officialSourceUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "migration_situation", length = 50)
    private MigrationSituation migrationSituation;

    @Column(nullable = false)
    private boolean required;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean active;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected DocumentRequirement() {
    }

    public DocumentRequirement(
            String code,
            String title,
            String description,
            String officialSourceUrl,
            MigrationSituation migrationSituation,
            boolean required,
            int displayOrder
    ) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.officialSourceUrl = officialSourceUrl;
        this.migrationSituation = migrationSituation;
        this.required = required;
        this.displayOrder = displayOrder;
        this.active = true;

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOfficialSourceUrl() {
        return officialSourceUrl;
    }

    public MigrationSituation getMigrationSituation() {
        return migrationSituation;
    }

    public boolean isRequired() {
        return required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}