CREATE TABLE customer_costs_by_category(
    customer_costs_id INTEGER NOT NULL REFERENCES customer_costs(id),
    categorized_costs_analytics_id INTEGER NOT NULL REFERENCES categorized_costs_analytics(id)
);

CREATE INDEX ON customer_costs_by_category(categorized_costs_analytics_id);