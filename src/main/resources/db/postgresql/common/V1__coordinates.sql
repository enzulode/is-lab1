CREATE SEQUENCE IF NOT EXISTS coordinates_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE coordinates
(
    id          BIGINT                      NOT NULL,
    created_by  VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by VARCHAR(255)                NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    x           FLOAT,
    y           INTEGER                     NOT NULL,

    CONSTRAINT pk_coordinates PRIMARY KEY (id)
);
