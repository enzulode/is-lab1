CREATE SEQUENCE IF NOT EXISTS employee_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE employee
(
    id          BIGINT                      NOT NULL,
    created_by  VARCHAR(255)                NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by VARCHAR(255)                NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    middle_name VARCHAR(255),
    birth_date  TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_employee PRIMARY KEY (id)
);