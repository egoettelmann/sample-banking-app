# Sample Banking App

Sample project implementing a banking REST Web Service.

Requirements for the project, their fulfillment and proposed improvements can be found here:
[REQUIREMENTS.md](REQUIREMENTS.md).

## Usage

### Run the Stack

Simply start the application through Docker Compose:
```shell script
docker-compose up --build -V
```

This will build the images and run the following services:
 - `api` on port 9090, the banking API to list accounts, perform payments, etc.
 - `api-db`, the database for the banking API (PostgreSQL)
 - `auth` on port 9091, the authentication API to generate JWT tokens and edit user info
 - `auth-db`, the database for the authentication API (PostgreSQL)

### Login

First you need to generate a JWT token on the authentication API.
You can do that through the Swagger UI:
 - [http://localhost:9091/swagger-ui.html](http://localhost:9091/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/authentication-controller/login)

Following users are configured:
 - `user1@test.com` / `password1`
 - `user2@test.com` / `password2`

You will find the generated JWT in the response.

This token is required for configuring the `Authorization` header on the banking API.
You can do this through the Swagger UI (`Authorize` button):
 - <http://localhost:9090/swagger-ui.html>

The same token can be used on the authentication API to update the user info.

### Play around

Following accounts are configured to create and delete payments:
 - `user1@test.com`:
   - `LU510011111111111111`, with an initial balance of `1,000.00 EUR`
   - `LU090012222222222222`, with an initial balance of `0.00 EUR`
 - `user2@test.com`
   - `LU640013333333333333`, with an initial balance of `1,000.00 EUR`
   - `LU220014444444444444`, with an initial balance of `0.00 EUR`

## End-2-end tests

The end-2-end tests are written with Mocha/Chai and can be executed within a Docker container.

```shell script
docker-compose -f docker-compose.yml -f docker-compose.test.yml up --build --abort-on-container-exit -V
```

This will start the entire stack, with the additional test container.
Once the tests done, all containers will stop, with the exit code of the test.

## Project overview

The app is split in 2 services:
 - `banking-api`, a SpringBoot application backed by a PostgreSQL database
   - more details are available here: [sample-banking-api/README.md](./sample-banking-api/README.md)
 - `auth-api`, a SpringBoot application backed by a PostgreSQL database
   - more details are available here: [sample-auth-api/README.md](./sample-auth-api/README.md)

Both applications follow a [package-by-component](https://dzone.com/articles/package-component-and) structure.

### Technical stack

 - JWT Authentication 
   - tokens are generated with `com.auth0:java-jwt`
   - both services share a common secret to encode/decode the token
 - DB migrations are managed with Flyway
   - with the `prod` profile, `Flyway` is disabled and databases are setup through dumps
 - Mapping is done with `Mapstruct`
 - Rest error handling is done with `org.zalando:problem-spring-web`
 - Rest endpoints are described with `springdoc-openapi` (Swagger for OpenAPI v3 specs)
 - Password policy relies on `Passay` 
