package com.chega.document;

import java.time.Instant;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "user_document_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_document_progress",
                        columnNames = {
                                "user_id",
                                "document_requirement_id"
                        }
                )
        }
)
public class UserDocumentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "document_requirement_id",
            nullable = false
    )
    private DocumentRequirement documentRequirement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentProgressStatus status;

    @Column(length = 500)
    private String notes;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserDocumentProgress() {
    }

    public UserDocumentProgress(
            User user,
            DocumentRequirement documentRequirement
    ) {
        this.user = user;
        this.documentRequirement = documentRequirement;
        this.status = DocumentProgressStatus.PENDING;

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void update(
            DocumentProgressStatus status,
            String notes
    ) {
        this.status = status;
        this.notes = normalizeNotes(notes);

        if (status == DocumentProgressStatus.COMPLETED) {
            if (this.completedAt == null) {
                this.completedAt = Instant.now();
            }
        } else {
            this.completedAt = null;
        }
    }

    private String normalizeNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }

        return notes
                .trim()
                .replaceAll("\\s+", " ");
    }

    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public DocumentRequirement getDocumentRequirement() {
        return documentRequirement;
    }

    public DocumentProgressStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}