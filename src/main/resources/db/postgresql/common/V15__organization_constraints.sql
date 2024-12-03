ALTER TABLE organization ALTER COLUMN name SET NOT NULL;
ALTER TABLE organization ADD CONSTRAINT org_name_not_blank CHECK ( name <> '' );

ALTER TABLE organization ALTER COLUMN coordinates_id SET NOT NULL;

ALTER TABLE organization ALTER COLUMN creation_date SET NOT NULL;

ALTER TABLE organization ALTER COLUMN official_address_id SET NOT NULL;

ALTER TABLE organization ADD CONSTRAINT org_annual_turnover_positive CHECK ( annual_turnover > 0 );

ALTER TABLE organization ADD CONSTRAINT org_employees_count_positive CHECK ( employees_count > 0 );

ALTER TABLE organization ADD CONSTRAINT org_rating_positive CHECK ( rating > 0 );

ALTER TABLE organization ALTER COLUMN full_name TYPE VARCHAR(2047);
ALTER TABLE organization ALTER COLUMN full_name SET NOT NULL;
ALTER TABLE organization ADD CONSTRAINT org_full_name_length CHECK ( length(full_name) <= 1658 );

ALTER TABLE organization ALTER COLUMN postal_address_id SET NOT NULL;
