CREATE TABLE user_tokens(
    user_id INTEGER NOT NULL,
    bank_id INTEGER NOT NULL,
    token VARCHAR NOT NULL,

    PRIMARY KEY (user_id, bank_id)
);