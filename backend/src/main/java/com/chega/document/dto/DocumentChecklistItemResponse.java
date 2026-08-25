package com.chega.document.dto;

import java.time.Instant;

import com.chega.document.DocumentProgressStatus;

public record DocumentChecklistItemResponse(

        Long requirementId,
        String code,
        String title,
        String description,
        String officialSourceUrl,
        boolean required,
        DocumentProgressStatus status,
        String notes,
        Instant completedAt

) {
}