CREATE TABLE user_bank(
    user_id INTEGER NOT NULL REFERENCES user_details(id),
    bank_id INTEGER NOT NULL REFERENCES bank(id),

    PRIMARY KEY (user_id, bank_id)
);