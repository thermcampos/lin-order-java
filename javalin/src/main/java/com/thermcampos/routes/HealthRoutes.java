package com.thermcampos.routes;

import com.thermcampos.health.HealthCheckHandler;

import io.javalin.config.JavalinConfig;

public class HealthRoutes {
  public static void register(JavalinConfig config) {
    config.routes.get("/health", ctx -> new HealthCheckHandler().getHealth(ctx));
  }
}
