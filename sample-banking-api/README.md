# Sample Banking API

## Configuration options

### Available Spring profiles

Feature flags:
 - `no-auth` disables any need for authentication by creating a default user on each request

Environments:
 - `local` used to run the application in standalone mode, with following features:
   - `no-auth`
 - `docker` used when running the service in a Docker container
 - `prod` used when running the entire stack (through Docker Compose)

## Development tips

During development, the application can be run in standalone mode:

```shell script
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
