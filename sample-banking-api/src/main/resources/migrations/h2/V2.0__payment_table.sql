CREATE TABLE "payment"
(
    "id"                         LONG AUTO_INCREMENT PRIMARY KEY,
    "reference"                  VARCHAR(100) UNIQUE,
    "amount"                     NUMERIC,
    "currency"                   VARCHAR(10),
    "origin_account_number"      VARCHAR(100),
    "beneficiary_account_number" VARCHAR(100),
    "beneficiary_name"           TEXT,
    "communication"              TEXT,
    "creation_date"              DATETIME2,
    "status"                     VARCHAR(20)
)
