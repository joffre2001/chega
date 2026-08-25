package com.chega.profile.dto;

import java.util.List;

import com.chega.profile.MigrationSituation;
import com.chega.profile.PrimaryGoal;

public record ProfileOptionsResponse(

        List<MigrationSituation> migrationSituations,
        List<PrimaryGoal> primaryGoals

) {
}