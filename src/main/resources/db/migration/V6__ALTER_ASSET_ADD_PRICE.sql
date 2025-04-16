SET search_path = auth_server, pg_catalog;

ALTER TABLE assets
ADD COLUMN price DOUBLE PRECISION NOT NULL DEFAULT 0.0;