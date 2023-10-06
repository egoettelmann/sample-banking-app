CREATE TABLE "claim"
(
    "id"        LONG AUTO_INCREMENT PRIMARY KEY,
    "value"     VARCHAR(100),
    "user_id"   LONG NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user" ("id"),
    CONSTRAINT UK_CLAIM_VALUE_USER_ID UNIQUE ("value", "user_id")
)
