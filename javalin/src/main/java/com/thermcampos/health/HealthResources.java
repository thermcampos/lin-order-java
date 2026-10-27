package com.thermcampos.health;

import io.javalin.config.JavalinConfig;

public class HealthResources {

  public static void register(JavalinConfig c) {
    c.routes.get("/health", ctx -> new HealthService().getHealth(ctx));
  }
}

