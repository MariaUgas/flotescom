CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE trucks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    model VARCHAR(100),
    capacity_limit DOUBLE PRECISION,
    current_load DOUBLE PRECISION DEFAULT 0.0,
    status VARCHAR(20) DEFAULT 'UNLOADED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE loads (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    truck_id UUID NOT NULL,
    volume DOUBLE PRECISION,
    description TEXT,
    load_timestamp TIMESTAMP,
    unload_timestamp TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (truck_id) REFERENCES trucks(id)
);