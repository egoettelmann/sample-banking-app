# Sample Auth API

## Configuration options

### Available Spring profiles

Environments:
 - `local` used to run the application in standalone mode, with following features:
   - embedded H2 in-memory database 
 - `docker` used when running the service in a Docker container
   - embedded H2 in-memory database
 - `prod` used when running the entire stack (through Docker Compose)

## Development tips

During development, the application can be run in standalone mode:

```shell script
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
