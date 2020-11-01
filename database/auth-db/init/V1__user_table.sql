CREATE TABLE "user"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "username"       VARCHAR(100) UNIQUE,
    "password"       TEXT,
    "address"        TEXT
);
