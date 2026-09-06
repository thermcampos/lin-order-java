# AGENTS.md

Guidelines for AI coding agents working on this repository.

## Project Overview

This is a **small e-commerce REST API** built with the minimalist [Javalin](https://javalin.io/) framework. The defining philosophy of this project is to be **simple and light**.

## Core Principles (must follow)

1. **No ORM.** Never introduce Hibernate, JPA, Exposed, or any ORM. When persistence is added, use plain JDBC or a thin SQL helper (e.g., plain `DataSource` + handwritten SQL).
2. **No heavy frameworks.** No Spring, no Guice, no dependency-injection containers. Wire dependencies manually through constructors.
3. **Minimal dependencies.** Do not add new dependencies unless strictly necessary. Justify any addition in the PR/commit.
4. **Keep it simple.** Prefer plain, readable code over abstractions. Avoid design-pattern ceremony unless it clearly pays off.

## Tech Stack

- Java 25 (Maven, wrapper included: `./mvnw`)
- Javalin 7 (`io.javalin:javalin`)
- SLF4J Simple for logging
- JUnit 4 for tests
- Fat jar built via `maven-assembly-plugin` (main class: `com.thermcampos.App`)

## Build, Test, Run

```bash
./mvnw test          # run tests
./mvnw package       # build (produces fat jar in target/)
./mvnw compile       # compile only
```

Always run `./mvnw test` after making changes.

## Project Structure

- Base package: `com.thermcampos`
- `App.java` - entry point; routes are registered here
- Feature code lives in sub-packages by domain (e.g., `health/`, and future `product/`, `order/`)
- Handlers follow the existing style: static methods taking Javalin `Context` (see `health/HealthCheckHandler.java`)

## Conventions

- Match the existing code style (4-space indent, handler-per-concern).
- Handlers should be thin; put logic in plain Java classes that are easy to unit test.
- The server runs on port **8080** by default.
