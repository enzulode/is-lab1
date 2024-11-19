CREATE OR REPLACE FUNCTION hire_employee_to_organization(org_id integer, e_id bigint)
    RETURNS void AS $$
BEGIN
    INSERT INTO employee_organizations (employee_id, organizations_id)
    VALUES (e_id, org_id);
END;
$$
LANGUAGE plpgsql;
