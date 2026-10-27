package com.thermcampos.health;

import io.javalin.config.JavalinConfig;

public class HealthResources {

  public static void register(JavalinConfig c, HealthService healthService) {
    c.routes.get("/health", healthService::getHealth);
  }
}
