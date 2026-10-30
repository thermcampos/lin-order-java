package com.thermcampos.web;

import com.thermcampos.validation.NotFoundException;
import com.thermcampos.validation.ValidationException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlers {

  private static final Logger log = LoggerFactory.getLogger(ErrorHandlers.class);

  public static void register(JavalinConfig c) {
    // Our own field validation (payload checks)
    c.routes.exception(ValidationException.class, (e, ctx) -> {
      String error = e.getErrorKey() + ": " + e.getMessage();
      log.warn("Validation failed: {}", error);
      ctx.status(HttpStatus.BAD_REQUEST)
          .json(Map.of("message", "Validation failed", "error", error));
    });

    // Not found entities (findByid)
    c.routes.exception(NotFoundException.class, (e, ctx) -> {
      String error = e.getEntity() + " was not found in the database: " + e.getMessage();
      log.warn("Entity not found: {}", error);
      ctx.status(HttpStatus.NOT_FOUND)
          .json(Map.of("message", "Entity not found", "error", error));
    });

    // Javalin's validation (e.g. ctx.pathParamAsClass(...).check(...)) - different class, same name
    c.routes.exception(io.javalin.validation.ValidationException.class, (e, ctx) -> {
      String error = e.getErrors().entrySet().stream()
          .map(entry -> entry.getKey() + ": " + entry.getValue().stream()
              .map(v -> v.getMessage())
              .collect(Collectors.joining(", ")))
          .collect(Collectors.joining("; "));
      log.warn("Invalid request: {}", error);
      ctx.status(HttpStatus.BAD_REQUEST)
          .json(Map.of("message", "Invalid request", "error", error));
    });

    // Anything else is a server bug or an infrastructure failure: log it, don't leak details
    c.routes.exception(Exception.class, (e, ctx) -> {
      log.error("Unhandled error on {} {}", ctx.method(), ctx.path(), e);
      ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .json(Map.of("message", "Internal server error"));
    });
  }
}
