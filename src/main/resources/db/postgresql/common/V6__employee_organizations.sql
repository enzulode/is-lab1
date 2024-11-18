CREATE TABLE employee_organizations
(
    employee_id      BIGINT  NOT NULL,
    organizations_id INTEGER NOT NULL,

    CONSTRAINT pk_employee_organizations PRIMARY KEY (employee_id, organizations_id)
);

ALTER TABLE employee_organizations
    ADD CONSTRAINT fk_emporg_on_employee FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE employee_organizations
    ADD CONSTRAINT fk_emporg_on_organization FOREIGN KEY (organizations_id) REFERENCES organization (id);
