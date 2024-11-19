CREATE OR REPLACE FUNCTION organization_total_rating()
    RETURNS double precision AS $$
DECLARE
    total_rating numeric;
BEGIN
    SELECT SUM(rating) INTO total_rating FROM organization;
    RETURN total_rating;
END;
$$
LANGUAGE plpgsql
