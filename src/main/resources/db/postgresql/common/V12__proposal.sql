CREATE SEQUENCE IF NOT EXISTS proposal_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE proposal
(
    id          BIGINT                      NOT NULL,
    created_by  VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by VARCHAR(255)                NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status      VARCHAR(255),
    user_id     VARCHAR(255),

    CONSTRAINT pk_proposal PRIMARY KEY (id)
);