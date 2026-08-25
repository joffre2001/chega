package com.chega.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chega.document.dto.DocumentChecklistItemResponse;
import com.chega.document.dto.UpdateDocumentProgressRequest;
import com.chega.exception.DocumentRequirementNotFoundException;
import com.chega.profile.MigrantProfile;
import com.chega.profile.MigrantProfileRepository;
import com.chega.profile.MigrationSituation;
import com.chega.user.User;
import com.chega.user.UserRepository;

class DocumentChecklistServiceTest {

    private DocumentRequirementRepository requirementRepository;
    private UserDocumentProgressRepository progressRepository;
    private UserRepository userRepository;
    private MigrantProfileRepository profileRepository;
    private DocumentChecklistService checklistService;

    @BeforeEach
    void setUp() {
        requirementRepository = mock(DocumentRequirementRepository.class);

        progressRepository = mock(UserDocumentProgressRepository.class);

        userRepository = mock(UserRepository.class);
        profileRepository = mock(MigrantProfileRepository.class);

        checklistService = new DocumentChecklistService(
                requirementRepository,
                progressRepository,
                userRepository,
                profileRepository);
    }

    @Test
    void shouldUpdateDocumentProgress() {
        String email = "teste@chega.com";
        Long userId = 1L;
        Long requirementId = 2L;
        Instant completedAt = Instant.now();

        User user = mock(User.class);
        MigrantProfile profile = mock(MigrantProfile.class);
        DocumentRequirement requirement = mock(DocumentRequirement.class);

        UserDocumentProgress progress = mock(UserDocumentProgress.class);

        UpdateDocumentProgressRequest request = new UpdateDocumentProgressRequest(
                DocumentProgressStatus.COMPLETED,
                "CPF conferido pelo usuario");

        when(userRepository.findByEmailIgnoreCase(email))
                .thenReturn(Optional.of(user));

        when(user.getId()).thenReturn(userId);

        when(profileRepository.findByUserEmailIgnoreCase(email))
                .thenReturn(Optional.of(profile));

        when(profile.getMigrationSituation())
                .thenReturn(MigrationSituation.RESIDENCE_PERMIT);

        when(requirementRepository.findApplicableRequirementById(
                requirementId,
                MigrationSituation.RESIDENCE_PERMIT)).thenReturn(Optional.of(requirement));

        when(progressRepository
                .findByUserIdAndDocumentRequirementId(
                        userId,
                        requirementId))
                .thenReturn(Optional.of(progress));

        when(progressRepository.save(progress))
                .thenReturn(progress);

        when(requirement.getId()).thenReturn(requirementId);
        when(requirement.getCode()).thenReturn("CPF");
        when(requirement.getTitle()).thenReturn(
                "Cadastro de Pessoa Física (CPF)");
        when(requirement.getDescription()).thenReturn(
                "Confira sua inscrição no CPF.");
        when(requirement.getOfficialSourceUrl()).thenReturn(
                "https://servicos.receita.fazenda.gov.br/");
        when(requirement.isRequired()).thenReturn(false);

        when(progress.getStatus())
                .thenReturn(DocumentProgressStatus.COMPLETED);
        when(progress.getNotes())
                .thenReturn("CPF conferido pelo usuario");
        when(progress.getCompletedAt()).thenReturn(completedAt);

        DocumentChecklistItemResponse response = checklistService.updateProgress(
                email,
                requirementId,
                request);

        assertEquals("CPF", response.code());
        assertEquals(
                DocumentProgressStatus.COMPLETED,
                response.status());
        assertEquals(completedAt, response.completedAt());

        verify(progress).update(
                DocumentProgressStatus.COMPLETED,
                "CPF conferido pelo usuario");

        verify(progressRepository).save(progress);
    }

    @Test
    void shouldRejectDocumentThatIsNotApplicableToProfile() {
        String email = "teste@chega.com";
        Long requirementId = 999L;

        User user = mock(User.class);
        MigrantProfile profile = mock(MigrantProfile.class);

        UpdateDocumentProgressRequest request = new UpdateDocumentProgressRequest(
                DocumentProgressStatus.COMPLETED,
                null);

        when(userRepository.findByEmailIgnoreCase(email))
                .thenReturn(Optional.of(user));

        when(user.getId()).thenReturn(1L);

        when(profileRepository.findByUserEmailIgnoreCase(email))
                .thenReturn(Optional.of(profile));

        when(profile.getMigrationSituation())
                .thenReturn(MigrationSituation.RESIDENCE_PERMIT);

        when(requirementRepository.findApplicableRequirementById(
                requirementId,
                MigrationSituation.RESIDENCE_PERMIT)).thenReturn(Optional.empty());

        assertThrows(
                DocumentRequirementNotFoundException.class,
                () -> checklistService.updateProgress(
                        email,
                        requirementId,
                        request));

        verify(progressRepository, never()).save(any());
    }
}