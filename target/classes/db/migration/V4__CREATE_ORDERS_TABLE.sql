SET search_path = auth_server, pg_catalog;

CREATE TABLE orders (
        id SERIAL PRIMARY KEY,
        price DOUBLE PRECISION NOT NULL,
        quantity DOUBLE PRECISION NOT NULL,
        type VARCHAR(10) NOT NULL,
        created_at TIMESTAMP DEFAULT NOW(),

        asset_id BIGINT NOT NULL,
        user_id UUID NOT NULL,

        CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id),
        CONSTRAINT fk_asset FOREIGN KEY(asset_id) REFERENCES assets(id)
);