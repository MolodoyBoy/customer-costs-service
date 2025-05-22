CREATE TABLE categorized_costs_analytics(
    id SERIAL PRIMARY KEY,
    amount NUMERIC,
    percent NUMERIC,
    transactions_count INTEGER,
    category_id INTEGER NOT NULL REFERENCES costs_category(id),
    period_cost_analytics_id INTEGER NOT NULL REFERENCES period_costs_analytics(id)
);

CREATE UNIQUE INDEX ON categorized_costs_analytics(period_cost_analytics_id, category_id);