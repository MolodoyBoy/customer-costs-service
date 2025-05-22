CREATE TABLE user_spending(
    max_amount NUMERIC,
    period INTEGER NOT NULL,
    bank_id INTEGER REFERENCES bank(id),
    current_amount NUMERIC NOT NULL DEFAULT 0.0,
    user_id INTEGER REFERENCES user_details(id),

    PRIMARY KEY (user_id, bank_id, period)
);