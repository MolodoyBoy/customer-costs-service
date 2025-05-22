CREATE TABLE monobank_user_accounts(
    account_id VARCHAR NOT NULL,
    user_id INTEGER NOT NULL
);

CREATE INDEX on monobank_user_accounts(account_id);