CREATE TABLE costs_category(
    id SERIAL PRIMARY KEY,
    description VARCHAR NOT NULL
);

INSERT INTO costs_category VALUES
            (1, 'Translations'),
            (2, 'Online stores'),
            (3, 'Payments to the budget'),
            (4, 'Taxi'),
            (5, 'Beauty'),
            (6, 'Restaurants, cafes, bars'),
            (7, 'Education'),
            (8, 'Medical services'),
            (9, 'Supermarkets and products'),
            (10, 'Pharmacies')