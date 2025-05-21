CREATE TABLE costs_category_embedding (
    category_id INTEGER PRIMARY KEY REFERENCES costs_category(id),
    embedding DOUBLE PRECISION[] NOT NULL
);