CREATE TABLE bank_account
(
    id             LONG AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(100) UNIQUE,
    account_name   TEXT,
    user_id        LONG
)
