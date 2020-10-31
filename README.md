# Sample Banking App

Sample project implementing a banking REST Web Service.

Requirements for the project are defined in [REQUIREMENTS.md](REQUIREMENTS.md).

## Usage

### Run the Stack

Simply start the application through Docker Compose:
```shell script
docker-compose up --build
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

## Project overview

The app is split in 2 services:
 - `banking-api`, a SpringBoot application backed by a PostgreSQL database
   - more details are available here: [sample-banking-api/README.MD](./sample-banking-api/README.md)
 - `auth-api`, a SpringBoot application backed by a PostgreSQL database
   - more details are available here: [sample-auth-api/README.MD](./sample-auth-api/README.md)

## Configuration options

### Available Spring profiles

Feature flags:
 - `no-auth` disables any need for authentication by creating a default user on each request

Environments:
 - `local` runs the application in standalone mode with following features:
   - `no-auth`
 - `docker`
 - `prod`

## Development tips

During development, the application can be run in standalone mode:

```shell script
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
