package com.thermcampos.health;

import io.javalin.config.JavalinConfig;

public class HealthRoutes {
  public static void register(JavalinConfig c) {
    c.routes.get("/health", ctx -> new HealthCheckHandler().getHealth(ctx));
  }
}
