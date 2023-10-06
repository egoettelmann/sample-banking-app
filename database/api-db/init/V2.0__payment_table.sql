CREATE TABLE "payment"
(
    "id"                         BIGSERIAL PRIMARY KEY,
    "reference"                  VARCHAR(100) UNIQUE,
    "amount"                     NUMERIC,
    "currency"                   VARCHAR(10),
    "origin_account_number"      VARCHAR(100),
    "beneficiary_account_number" VARCHAR(100),
    "beneficiary_name"           TEXT,
    "communication"              TEXT,
    "creation_date"              TIMESTAMP WITH TIME ZONE,
    "status"                     VARCHAR(20)
)
