CREATE TABLE "bank_account"
(
    "id"            BIGSERIAL PRIMARY KEY,
    "number"        VARCHAR(100) UNIQUE,
    "name"          TEXT,
    "currency"      VARCHAR(10),
    "owner"         VARCHAR(100)
);
