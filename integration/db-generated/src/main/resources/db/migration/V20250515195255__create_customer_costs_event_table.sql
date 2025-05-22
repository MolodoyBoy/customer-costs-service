CREATE TABLE customer_costs_events(
    id BIGSERIAL PRIMARY KEY,
    processed BOOLEAN DEFAULT FALSE,
    customer_costs_id INTEGER NOT NULL REFERENCES customer_costs(id)
);