CREATE TABLE "bank_account"
(
    "id"            LONG AUTO_INCREMENT PRIMARY KEY,
    "number"        VARCHAR(100) UNIQUE,
    "name"          TEXT,
    "currency"      VARCHAR(10),
    "user_id"       LONG
)
