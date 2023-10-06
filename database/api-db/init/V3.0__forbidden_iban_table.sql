CREATE TABLE "forbidden_iban"
(
    "id"    BIGSERIAL PRIMARY KEY,
    "iban"  VARCHAR(100) UNIQUE
)
