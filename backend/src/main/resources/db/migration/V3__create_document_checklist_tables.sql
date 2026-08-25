CREATE TABLE document_requirements (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(80) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),

    migration_situation VARCHAR(50),
    required BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_document_display_order
        CHECK (display_order >= 0)
);

CREATE TABLE user_document_progress (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    document_requirement_id BIGINT NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    notes VARCHAR(500),
    completed_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_document_progress_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_document_progress_requirement
        FOREIGN KEY (document_requirement_id)
        REFERENCES document_requirements (id)
        ON DELETE CASCADE,

    CONSTRAINT uq_user_document_progress
        UNIQUE (user_id, document_requirement_id),

    CONSTRAINT chk_document_progress_status
        CHECK (
            status IN (
                'PENDING',
                'IN_PROGRESS',
                'COMPLETED',
                'NOT_APPLICABLE'
            )
        )
);

CREATE INDEX idx_document_requirements_situation
    ON document_requirements (migration_situation);

CREATE INDEX idx_document_progress_user
    ON user_document_progress (user_id);