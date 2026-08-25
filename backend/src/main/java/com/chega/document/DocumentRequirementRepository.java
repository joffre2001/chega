package com.chega.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chega.profile.MigrationSituation;

public interface DocumentRequirementRepository
        extends JpaRepository<DocumentRequirement, Long> {

    @Query("""
            SELECT requirement
            FROM DocumentRequirement requirement
            WHERE requirement.active = true
              AND (
                    requirement.migrationSituation IS NULL
                    OR requirement.migrationSituation = :situation
              )
            ORDER BY requirement.displayOrder, requirement.title
            """)
    List<DocumentRequirement> findApplicableRequirements(
            @Param("situation")
            MigrationSituation situation
    );

    @Query("""
            SELECT requirement
            FROM DocumentRequirement requirement
            WHERE requirement.id = :requirementId
              AND requirement.active = true
              AND (
                    requirement.migrationSituation IS NULL
                    OR requirement.migrationSituation = :situation
              )
            """)
    Optional<DocumentRequirement> findApplicableRequirementById(
            @Param("requirementId")
            Long requirementId,

            @Param("situation")
            MigrationSituation situation
    );
}