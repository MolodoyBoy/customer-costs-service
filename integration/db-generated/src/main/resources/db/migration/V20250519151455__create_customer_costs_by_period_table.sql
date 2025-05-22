CREATE TABLE customer_costs_by_period(
    customer_costs_id INTEGER NOT NULL REFERENCES customer_costs(id),
    period_costs_analytics_id INTEGER NOT NULL REFERENCES period_costs_analytics(id)
);

CREATE INDEX ON customer_costs_by_period(period_costs_analytics_id);