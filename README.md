# Sample Banking API

Sample project implementing a banking REST Web Service.

Requirements for the project are defined in [REQUIREMENTS.md](REQUIREMENTS.md).

## Usage

TODO: 

## Configuration options

### Available Spring profiles

Feature flags:
 - `no-auth` disables any need for authentication by creating a default user on each request

Environments:
 - `local` runs the application in standalone mode with following features:
   - `no-auth`

## Development tips

During development, the application can be run in standalone mode:

```shell script
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
