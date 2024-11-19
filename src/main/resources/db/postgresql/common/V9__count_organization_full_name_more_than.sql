CREATE OR REPLACE FUNCTION count_organization_full_name_more_than(c_full_name varchar)
    RETURNS integer AS $$
DECLARE
    count_objects integer;
BEGIN
    SELECT COUNT(*) INTO count_objects
    FROM organization
    WHERE full_name > c_full_name;

    RETURN count_objects;
END;
$$
LANGUAGE plpgsql;
