CREATE TABLE migrant_profiles (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL UNIQUE,

    nationality VARCHAR(100) NOT NULL,
    current_city VARCHAR(120) NOT NULL,
    arrival_date DATE,

    migration_situation VARCHAR(40) NOT NULL,
    primary_goal VARCHAR(30) NOT NULL,

    consent_given_at TIMESTAMPTZ NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_migrant_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_migration_situation
        CHECK (
            migration_situation IN (
                'RESIDENCE_PERMIT',
                'REFUGEE',
                'ASYLUM_SEEKER',
                'STUDENT',
                'FAMILY_REUNIFICATION',
                'IN_REGULARIZATION',
                'OTHER'
            )
        ),

    CONSTRAINT chk_primary_goal
        CHECK (
            primary_goal IN (
                'DOCUMENTATION',
                'WORK',
                'HEALTH',
                'EDUCATION',
                'PORTUGUESE',
                'HOUSING',
                'FAMILY',
                'OTHER'
            )
        )
);

CREATE INDEX idx_migrant_profiles_current_city
    ON migrant_profiles(current_city);

CREATE INDEX idx_migrant_profiles_primary_goal
    ON migrant_profiles(primary_goal);