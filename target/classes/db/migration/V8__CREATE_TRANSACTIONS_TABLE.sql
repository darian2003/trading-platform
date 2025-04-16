SET search_path = auth_server, pg_catalog;

CREATE TABLE transactions (
        id SERIAL PRIMARY KEY,
        buy_order_id INTEGER,
        sell_order_id INTEGER,
        asset_id BIGINT NOT NULL,
        price DOUBLE PRECISION NOT NULL,
        quantity DOUBLE PRECISION NOT NULL,
        timestamp TIMESTAMP DEFAULT NOW(),


        CONSTRAINT fk_transaction_buy_order FOREIGN KEY(buy_order_id) REFERENCES orders(id),
        CONSTRAINT fk_transaction_sell_order FOREIGN KEY(sell_order_id) REFERENCES orders(id),
        CONSTRAINT fk_transaction_asset FOREIGN KEY(asset_id) REFERENCES assets(id)
);