CREATE TABLE "user"
(
    "id"        LONG AUTO_INCREMENT PRIMARY KEY,
    "username"  VARCHAR(100) UNIQUE,
    "password"  TEXT,
    "address"   TEXT,
    CONSTRAINT UK_USER_USERNAME UNIQUE ("username")
)
