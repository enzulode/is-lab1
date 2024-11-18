CREATE TABLE organization_employees
(
    employees_id    BIGINT  NOT NULL,
    organization_id INTEGER NOT NULL,

    CONSTRAINT pk_organization_employees PRIMARY KEY (employees_id, organization_id)
);

ALTER TABLE organization_employees
    ADD CONSTRAINT fk_orgemp_on_employee FOREIGN KEY (employees_id) REFERENCES employee (id);

ALTER TABLE organization_employees
    ADD CONSTRAINT fk_orgemp_on_organization FOREIGN KEY (organization_id) REFERENCES organization (id);
