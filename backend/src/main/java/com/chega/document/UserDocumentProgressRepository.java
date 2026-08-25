package com.chega.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDocumentProgressRepository
        extends JpaRepository<UserDocumentProgress, Long> {

    List<UserDocumentProgress> findByUserId(Long userId);

    Optional<UserDocumentProgress>
            findByUserIdAndDocumentRequirementId(
                    Long userId,
                    Long documentRequirementId
            );
}