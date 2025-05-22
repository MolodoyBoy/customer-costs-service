CREATE TABLE period_costs_analytics(
    id SERIAL PRIMARY KEY,
    amount NUMERIC,
    period INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    difference_from_previous_month NUMERIC
);

CREATE UNIQUE INDEX ON period_costs_analytics(user_id, period);