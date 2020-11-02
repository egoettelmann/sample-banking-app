CREATE TABLE balance
(
    id         LONG AUTO_INCREMENT PRIMARY KEY,
    amount     NUMERIC,
    currency   VARCHAR(20),
    account_id LONG,
    status     VARCHAR(20)
)
