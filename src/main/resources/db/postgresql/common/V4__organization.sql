CREATE SEQUENCE IF NOT EXISTS organization_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE organization
(
    id                  INTEGER                     NOT NULL,
    created_by          VARCHAR(255)                NOT NULL,
    created_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by         VARCHAR(255)                NOT NULL,
    modified_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name                VARCHAR(255),
    coordinates_id      BIGINT,
    creation_date       TIMESTAMP WITHOUT TIME ZONE,
    official_address_id BIGINT,
    annual_turnover     INTEGER                     NOT NULL,
    employees_count     INTEGER                     NOT NULL,
    rating              DOUBLE PRECISION,
    full_name           VARCHAR(255),
    type                VARCHAR(255),
    postal_address_id   BIGINT,

    CONSTRAINT pk_organization PRIMARY KEY (id)
);

ALTER TABLE organization
    ADD CONSTRAINT FK_ORGANIZATION_ON_COORDINATES FOREIGN KEY (coordinates_id) REFERENCES coordinates (id);

ALTER TABLE organization
    ADD CONSTRAINT FK_ORGANIZATION_ON_OFFICIAL_ADDRESS FOREIGN KEY (official_address_id) REFERENCES address (id);

ALTER TABLE organization
    ADD CONSTRAINT FK_ORGANIZATION_ON_POSTAL_ADDRESS FOREIGN KEY (postal_address_id) REFERENCES address (id);