CREATE TABLE "bank_account"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "account_number" VARCHAR(100) UNIQUE,
    "account_name"   TEXT,
    "user_id"        BIGINT
);
