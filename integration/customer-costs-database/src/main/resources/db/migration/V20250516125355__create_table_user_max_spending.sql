CREATE TABLE user_max_spending(
    user_id INTEGER PRIMARY KEY REFERENCES user_details(id),
    max_amount NUMERIC NOT NULL,
    current_amount NUMERIC NOT NULL DEFAULT 0.0
)