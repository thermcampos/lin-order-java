# lin-order (Javalin)

A small e-commerce REST API built with the minimalist [Javalin](https://javalin.io/) framework. The goal of this project is to stay **simple and light**: no ORM, no heavy frameworks, no magic. Just Java, Javalin, and plain SQL when persistence is needed.

## Tech Stack

- **Java 25**
- **Javalin 7** - lightweight web framework
- **SLF4J Simple** - logging
- **Maven** - build tool (wrapper included)
- **JUnit 4** - tests

Deliberately **not** used: ORMs (Hibernate/JPA), DI frameworks (Spring/Guice), or any other heavy dependencies.

## Requirements

- JDK 25+

## Getting Started

```bash
# Run the app
./mvnw compile exec:java

# Or build a fat jar and run it
./mvnw package
java -jar target/javalin-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The server starts on **port 8080**.

## Endpoints

| Method | Path      | Description        |
|--------|-----------|--------------------|
| GET    | `/`       | Hello message      |
| GET    | `/health` | Health check (JSON) |

More e-commerce endpoints (products, orders, etc.) coming soon.

## Project Structure

```
src/main/java/com/thermcampos/
├── App.java                  # Entry point, route registration
└── health/
    └── HealthCheckHandler.java
```

## Running Tests

```bash
./mvnw test
```
