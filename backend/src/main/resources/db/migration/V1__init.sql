CREATE TABLE IF NOT EXISTS loaders (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    number VARCHAR(50) NOT NULL UNIQUE,
    capacity NUMERIC(10,3) NOT NULL CHECK (capacity >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS incidents (
    id SERIAL PRIMARY KEY,
    loader_id INTEGER NOT NULL REFERENCES loaders(id) ON DELETE RESTRICT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    description TEXT
);

CREATE INDEX idx_incidents_loader_id ON incidents(loader_id);