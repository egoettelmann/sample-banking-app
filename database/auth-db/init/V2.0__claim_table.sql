CREATE TABLE "claim"
(
    "id"        BIGSERIAL PRIMARY KEY,
    "value"     VARCHAR(100),
    "user_id"   BIGINT NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user" ("id"),
    CONSTRAINT UK_CLAIM_VALUE_USER_ID UNIQUE ("value", "user_id")
);
