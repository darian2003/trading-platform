SET search_path = auth_server, pg_catalog;

CREATE TABLE book (
                      id uuid,
                      title TEXT NOT NULL,
                      author TEXT NOT NULL,
                      PRIMARY KEY (id)
);