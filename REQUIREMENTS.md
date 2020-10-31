# Requirements

## Main requirements

- [x] Authentication
- [x] List the bank accounts
- [ ] Create single payment
- [ ] List all payments
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
- [ ] Provide a Docker build(s) to run the application in containers
- [ ] Enforce a password policy on user password update
- [x] Use a database migration framework
- [x] Provide a Swagger specification
- [ ] User model has an additional property "photo"
- [x] Enable CORS

## Further improvements

- [ ] User identification is based on technical id (auto-generated)
  - [ ] Banking API should have email or other business id to identify users
