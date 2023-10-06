CREATE TABLE "balance"
(
    "id"                BIGSERIAL PRIMARY KEY,
    "account_number"    VARCHAR(100),
    "value"             NUMERIC,
    "value_date"        TIMESTAMP WITH TIME ZONE,
    "status"            VARCHAR(20),
    CONSTRAINT uk_balance_account_number_value_date UNIQUE ("account_number", "value_date")
);
