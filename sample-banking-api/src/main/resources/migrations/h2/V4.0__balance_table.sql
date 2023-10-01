CREATE TABLE "balance"
(
    "id"             LONG AUTO_INCREMENT PRIMARY KEY,
    "account_number" VARCHAR(100),
    "value"          NUMERIC,
    "value_date"     DATETIME2,
    "status"         VARCHAR(20),
    CONSTRAINT uk_balance_account_number_value_date UNIQUE ("account_number", "value_date")
)
