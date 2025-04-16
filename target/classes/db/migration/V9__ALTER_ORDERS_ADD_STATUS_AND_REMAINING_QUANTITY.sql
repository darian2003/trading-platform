SET search_path = auth_server, pg_catalog;

ALTER TABLE orders
    ADD COLUMN status VARCHAR(20) DEFAULT 'Active';

ALTER TABLE orders
    ADD COLUMN remaining_quantity DOUBLE PRECISION;

-- Backfill existing rows (assumes all are active and fully open)
UPDATE orders
SET remaining_quantity = quantity;
