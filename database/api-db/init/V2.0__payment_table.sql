CREATE TABLE "payment"
(
    id                         BIGSERIAL PRIMARY KEY,
    amount                     NUMERIC,
    currency                   VARCHAR(20),
    giver_account_id           BIGINT,
    beneficiary_account_number TEXT,
    beneficiary_name           TEXT,
    communication              TEXT,
    creation_date              TIMESTAMP WITH TIME ZONE,
    status                     VARCHAR(20)
)
