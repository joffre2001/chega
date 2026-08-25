package com.chega.document;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chega.document.dto.DocumentChecklistItemResponse;
import com.chega.document.dto.UpdateDocumentProgressRequest;
import com.chega.exception.DocumentRequirementNotFoundException;
import com.chega.exception.InvalidCredentialsException;
import com.chega.exception.MigrantProfileNotFoundException;
import com.chega.profile.MigrantProfile;
import com.chega.profile.MigrantProfileRepository;
import com.chega.user.User;
import com.chega.user.UserRepository;

@Service
public class DocumentChecklistService {

    private final DocumentRequirementRepository requirementRepository;
    private final UserDocumentProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final MigrantProfileRepository profileRepository;

    public DocumentChecklistService(
            DocumentRequirementRepository requirementRepository,
            UserDocumentProgressRepository progressRepository,
            UserRepository userRepository,
            MigrantProfileRepository profileRepository
    ) {
        this.requirementRepository = requirementRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    public List<DocumentChecklistItemResponse> findCurrentChecklist(
            String authenticatedEmail
    ) {
        User user = findUser(authenticatedEmail);
        MigrantProfile profile = findProfile(authenticatedEmail);

        List<DocumentRequirement> requirements =
                requirementRepository.findApplicableRequirements(
                        profile.getMigrationSituation()
                );

        Map<Long, UserDocumentProgress> progressByRequirement =
                progressRepository.findByUserId(user.getId())
                        .stream()
                        .collect(Collectors.toMap(
                                progress -> progress
                                        .getDocumentRequirement()
                                        .getId(),
                                Function.identity()
                        ));

        return requirements.stream()
                .map(requirement -> toResponse(
                        requirement,
                        progressByRequirement.get(requirement.getId())
                ))
                .toList();
    }

    @Transactional
    public DocumentChecklistItemResponse updateProgress(
            String authenticatedEmail,
            Long requirementId,
            UpdateDocumentProgressRequest request
    ) {
        User user = findUser(authenticatedEmail);
        MigrantProfile profile = findProfile(authenticatedEmail);

        DocumentRequirement requirement =
                requirementRepository
                        .findApplicableRequirementById(
                                requirementId,
                                profile.getMigrationSituation()
                        )
                        .orElseThrow(
                                DocumentRequirementNotFoundException::new
                        );

        UserDocumentProgress progress =
                progressRepository
                        .findByUserIdAndDocumentRequirementId(
                                user.getId(),
                                requirementId
                        )
                        .orElseGet(() ->
                                new UserDocumentProgress(
                                        user,
                                        requirement
                                )
                        );

        progress.update(
                request.status(),
                request.notes()
        );

        UserDocumentProgress savedProgress =
                progressRepository.save(progress);

        return toResponse(requirement, savedProgress);
    }

    private User findUser(String authenticatedEmail) {
        return userRepository
                .findByEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(InvalidCredentialsException::new);
    }

    private MigrantProfile findProfile(
            String authenticatedEmail
    ) {
        return profileRepository
                .findByUserEmailIgnoreCase(authenticatedEmail)
                .orElseThrow(MigrantProfileNotFoundException::new);
    }

    private DocumentChecklistItemResponse toResponse(
            DocumentRequirement requirement,
            UserDocumentProgress progress
    ) {
        if (progress == null) {
            return new DocumentChecklistItemResponse(
                    requirement.getId(),
                    requirement.getCode(),
                    requirement.getTitle(),
                    requirement.getDescription(),
                    requirement.getOfficialSourceUrl(),
                    requirement.isRequired(),
                    DocumentProgressStatus.PENDING,
                    null,
                    null
            );
        }

        return new DocumentChecklistItemResponse(
                requirement.getId(),
                requirement.getCode(),
                requirement.getTitle(),
                requirement.getDescription(),
                requirement.getOfficialSourceUrl(),
                requirement.isRequired(),
                progress.getStatus(),
                progress.getNotes(),
                progress.getCompletedAt()
        );
    }
}