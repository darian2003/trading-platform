SET search_path = auth_server, pg_catalog;

ALTER TABLE holdings
ADD COLUMN reserved_quantity DOUBLE PRECISION DEFAULT 0.0;