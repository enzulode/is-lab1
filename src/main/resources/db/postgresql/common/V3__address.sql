CREATE SEQUENCE IF NOT EXISTS address_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE address
(
    id          BIGINT                      NOT NULL,
    created_by  VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by VARCHAR(255)                NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    street      VARCHAR(255),
    town_id     BIGINT                      NOT NULL,

    CONSTRAINT pk_address PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT fk_address_on_town FOREIGN KEY (town_id) REFERENCES location (id);
