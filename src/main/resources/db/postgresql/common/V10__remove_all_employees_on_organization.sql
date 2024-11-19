CREATE OR REPLACE FUNCTION remove_all_employees_on_organization(org_id integer)
    RETURNS void AS $$
BEGIN
    DELETE
    FROM employee_organizations
    WHERE employee_organizations.organizations_id = org_id;
END;
$$
LANGUAGE plpgsql;
