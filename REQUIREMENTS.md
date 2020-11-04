# Requirements

## Main requirements

- [x] Authentication
- [x] List the bank accounts
- [x] Create single payment
  - [x] The giver account should belong to the authenticated user
  - [x] Payments to the same account number should not be valid
  - [x] Payments that exceed the available balance of the account should not be valid
  - [x] Payments to this given list of forbidden accounts should not be valid
  - [x] Giver account balance should be decreased when a payment is executed ...
  - [x] ... and increased if the beneficiary is an account that belongs to the bank
  - [x] Call an external web service to validate the IBAN format of the beneficiary
- [ ] List all payments
  - [x] Provide a REST endpoint to list all created payments ordered by creation date
  - [ ] Provide a REST endpoint to list all created payments to a given beneficiaryAccountNumber and within a given period ordered by creation date
    - Should be easy to add by integrating QueryDSL
- [x] Delete payment
- [x] Update User info
- [ ] Logout endpoint
  - Invalidating JWT tokens with the current approach is not possible:
    - JWT should be validated against the auth API
    - the auth API should maintain a list of invalidated tokens 

## Optional requirements

- [x] Password should not be stored as plaintext in the database
- [x] Payment execution process should be part of a single transaction
- [x] Implement a pagination mechanism for the list of created payments
- [ ] Implement unit test coverage up to 100%
- [x] Provide automated integration tests
- [ ] Register a fraud tentative when the user attempts a payment to forbidden accounts and block the account in case of more than 5 fraud tentative.
  - One approach to implement this could be:
    - when catching the `ForbiddenIbanException`, place a lock on the user
    - the authorization filter should check if a lock is placed on the user
  - Another approach could be to invalidate the token and lock the account on the auth API
- [ ] Architecture authentication, account listing and payment in separate micro-services
  - Account listing and payments are still in the same service
  - It could be interesting to add a message broker to handle payments asynchronously
- [x] Provide a Docker build(s) to run the application in containers
- [x] Enforce a password policy on user password update
- [x] Use a database migration framework
- [x] Provide a Swagger specification
- [ ] User model has an additional property "photo"
  - To be done: add file upload and store as blob in DB
- [x] Enable CORS

## Further improvements

- [ ] Payments get never the `EXECUTED` status
  - An idea could be to run a batch at the end of the day to
    - execute all pending payments
    - update the `AVAILABLE` balance and delete the `END_OF_DAY` balance
- [ ] currency is not handled: sync and check currency of payments with balance
- [ ] User identification is based on technical id (auto-generated)
  - Banking API should have email or other business id to identify users
- [ ] Docker Compose: apps should wait that DBs are available before starting
- [ ] Replace the auth-api with a proper Authorization Server
  - An idea could be to integrate Keycloak (<https://hub.docker.com/r/jboss/keycloak/>) and seed it with a JSON import dump file
- [ ] Develop a Single Page App as frontend
