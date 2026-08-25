package com.chega.document.dto;

import com.chega.document.DocumentProgressStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDocumentProgressRequest(

        @NotNull(message = "O status do documento é obrigatório.")
        DocumentProgressStatus status,

        @Size(
                max = 500,
                message = "A observação deve ter no máximo 500 caracteres."
        )
        String notes

) {
}