# Requirements

## Main requirements

- [x] Authentication
- [x] List the bank accounts
- [ ] Create single payment
  - [x] The giver account should belong to the authenticated user
  - [x] Payments to the same account number should not be valid
  - [ ] Payments that exceed the available balance of the account should not be valid
  - [ ] Payments to this given list of forbidden accounts should not be valid
  - [ ] Giver account balance should be decreased when a payment is executed ...
  - [ ] ... and increased if the beneficiary is an account that belongs to the bank
  - [x] Call an external web service to validate the IBAN format of the beneficiary
- [ ] List all payments
  - [x] Provide a REST endpoint to list all created payments ordered by creation date
  - [ ] Provide a REST endpoint to list all created payments to a given beneficiaryAccountNumber and within a given period ordered by creation date
- [ ] Delete payment
- [ ] Update User info
- [ ] Logout endpoint

## Optional requirements

- [x] Password should not be stored as plaintext in the database
- [ ] Payment execution process should be part of a single transaction
- [x] Implement a pagination mechanism for the list of created payments
- [ ] Implement unit test coverage up to 100%
- [ ] Provide automated integration tests
- [ ] Register a fraud tentative when the user attempts a payment to forbidden accounts and block the account in case of more than 5 fraud tentative.
- [ ] Architecture authentication, account listing and payment in separate micro-services
- [x] Provide a Docker build(s) to run the application in containers
- [ ] Enforce a password policy on user password update
- [x] Use a database migration framework
- [x] Provide a Swagger specification
- [ ] User model has an additional property "photo"
- [x] Enable CORS

## Further improvements

- [ ] User identification is based on technical id (auto-generated)
  - Banking API should have email or other business id to identify users
- [ ] Replace the auth-api with a proper Authorization Server
  - An idea could be to integrate Keycloak (<https://hub.docker.com/r/jboss/keycloak/>) and seed it with a JSON import dump file
- [ ] Docker-compose: apps should wait that DBs are available before starting
