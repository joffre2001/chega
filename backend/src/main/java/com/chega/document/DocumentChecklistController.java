package com.chega.document;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chega.document.dto.DocumentChecklistItemResponse;
import com.chega.document.dto.UpdateDocumentProgressRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/documents/checklist")
public class DocumentChecklistController {

    private final DocumentChecklistService checklistService;

    public DocumentChecklistController(
            DocumentChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping
    public List<DocumentChecklistItemResponse> findCurrentChecklist(
            Authentication authentication) {
        return checklistService.findCurrentChecklist(
                authentication.getName());
    }

    @PutMapping("/{requirementId}")
    public DocumentChecklistItemResponse updateProgress(
            Authentication authentication,

            @PathVariable("requirementId") Long requirementId,

            @Valid @RequestBody UpdateDocumentProgressRequest request) {
        return checklistService.updateProgress(
                authentication.getName(),
                requirementId,
                request);
    }
}