CREATE TABLE "balance"
(
    "id"         BIGSERIAL PRIMARY KEY,
    "amount"     NUMERIC,
    "currency"   VARCHAR(20),
    "account_id" BIGINT,
    "status"     VARCHAR(20)
);
