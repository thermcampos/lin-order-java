package com.thermcampos.routes;

import com.thermcampos.health.HealthCheckHandler;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.config.JavalinConfig;

public class HealthRoutes {
  public static void register(JavalinConfig c, HikariDataSource ds) {
    c.routes.get("/health", ctx -> new HealthCheckHandler().getHealth(ctx, ds));
  }
}
