package com.thermcampos.health;

import com.thermcampos.config.AppConfig;
import com.thermcampos.logger.AppLogger;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class HealthService {
    
  public void getHealth(Context ctx) {
    var props = ctx.appData(AppConfig.PROPERTIES);
    var logger = new AppLogger(getClass(), props);
    var ds = ctx.appData(AppConfig.DATA_SOURCE);

    try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
      if (conn.isValid(3)) {
        stmt.execute("SELECT 1");

        ctx.status(HttpStatus.OK)
            .json(Map.of("status", "UP"));
      } else {
        ctx.status(HttpStatus.SERVICE_UNAVAILABLE)
            .json(Map.of("status", "DOWN", "error", "Connection is invalid."));
      }
    } catch (Exception e) {
      ctx.status(HttpStatus.SERVICE_UNAVAILABLE)
          .json(Map.of("status", "DOWN", "error", e.getMessage()));
    }
  }
}

