package com.thermcampos.health;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthService {

  private static final Logger log = LoggerFactory.getLogger(HealthService.class);

  private final DataSource ds;

  public HealthService(DataSource ds) {
    this.ds = ds;
  }

  public void getHealth(Context ctx) {
    try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
      if (conn.isValid(3)) {
        stmt.execute("SELECT 1");

        ctx.status(HttpStatus.OK)
            .json(Map.of("status", "UP"));
      } else {
        log.warn("Health check failed: connection is invalid");
        ctx.status(HttpStatus.SERVICE_UNAVAILABLE)
            .json(Map.of("status", "DOWN", "error", "Connection is invalid."));
      }
    } catch (Exception e) {
      log.warn("Health check failed", e);
      ctx.status(HttpStatus.SERVICE_UNAVAILABLE)
          .json(Map.of("status", "DOWN", "error", String.valueOf(e.getMessage())));
    }
  }
}
