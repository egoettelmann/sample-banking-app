CREATE TABLE payment (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    amount NUMERIC,
    currency VARCHAR(20),
    giver_account_id LONG,
    beneficiary_account_number TEXT,
    beneficiary_name TEXT,
    communication TEXT,
    creation_date DATETIME2,
    status VARCHAR(20)
)
