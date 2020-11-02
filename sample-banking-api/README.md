# Sample Banking API

## Configuration options

### Available Spring profiles

Feature flags:
 - `no-auth` disables any need for authentication by creating a default user on each request

Environments:
 - `local` used to run the application in standalone mode, with following features:
   - `no-auth` profile
   - embedded H2 in-memory database 
 - `docker` used when running the service in a Docker container
   - `no-auth` profile
   - embedded H2 in-memory database
 - `prod` used when running the entire stack (through Docker Compose)

## Development tips

During development, the application can be run in standalone mode:

```shell script
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
