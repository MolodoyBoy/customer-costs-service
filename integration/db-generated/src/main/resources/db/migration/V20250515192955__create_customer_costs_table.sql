CREATE TABLE customer_costs(
    id SERIAL PRIMARY KEY,
    bank_id INTEGER NOT NULL REFERENCES bank(id),
    user_id INTEGER NOT NULL REFERENCES user_details(id),
    category_id INTEGER NOT NULL REFERENCES costs_category(id),
    amount NUMERIC NOT NULL,
    description VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX ON customer_costs(user_id, bank_id);