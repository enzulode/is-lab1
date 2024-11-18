CREATE SEQUENCE IF NOT EXISTS location_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE location
(
    id          BIGINT                      NOT NULL,
    created_by  VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by VARCHAR(255)                NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    x           FLOAT,
    y           DOUBLE PRECISION            NOT NULL,
    z           BIGINT,

    CONSTRAINT pk_location PRIMARY KEY (id)
);