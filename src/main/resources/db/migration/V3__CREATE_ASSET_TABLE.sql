SET search_path = auth_server, pg_catalog;

CREATE TABLE assets (
                        id SERIAL PRIMARY KEY,
                        symbol VARCHAR(20) NOT NULL UNIQUE,
                        name VARCHAR(100) NOT NULL
);