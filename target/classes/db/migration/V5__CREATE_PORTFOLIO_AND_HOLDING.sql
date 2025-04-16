SET search_path = auth_server, pg_catalog;

CREATE TABLE portfolios (
        id SERIAL PRIMARY KEY,
        user_id UUID NOT NULL UNIQUE,
        created_at TIMESTAMP DEFAULT NOW(),
        updated_at TIMESTAMP DEFAULT NOW(),

        CONSTRAINT fk_portfolio_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE holdings (
        id SERIAL PRIMARY KEY,
        quantity DOUBLE PRECISION NOT NULL,

        portfolio_id BIGINT NOT NULL,
        asset_id BIGINT NOT NULL,

        created_at TIMESTAMP DEFAULT NOW(),
        updated_at TIMESTAMP DEFAULT NOW(),

        CONSTRAINT fk_holding_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolios(id),
        CONSTRAINT fk_holding_asset FOREIGN KEY (asset_id) REFERENCES assets(id)
);