# Calculator

Simple Spring Boot calculator API.

## Requirements

- Java 21
- Maven

## Running the application

```bash
mvn spring-boot:run
```

The default profile is `develop`.

## API

### Add two numbers

```http
POST /add
Content-Type: application/json
```

Request:

```json
{
  "val1": 12.0,
  "val2": 6.0
}
```

Response:

```json
{
  "value": 18.0
}
```

### Divide two numbers

```http
GET /div?val1=12.0&val2=6.0
```

Response:

```json
{
  "value": 2.0
}
```

## Swagger UI

Swagger UI is available in non-production profiles:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI and OpenAPI docs are disabled in the `production` profile.

## Useful Maven commands

```bash
mvn clean
mvn test
mvn spotbugs:check
mvn package
```