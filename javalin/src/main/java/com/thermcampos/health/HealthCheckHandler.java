package com.thermcampos.health;

import com.thermcampos.db.DbKeys;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class HealthCheckHandler {
    
  public void getHealth(Context ctx) {
    var ds = ctx.appData(DbKeys.DATA_SOURCE);
    try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
      if (conn.isValid(3)) {
        stmt.execute("SELECT 1");
        ctx.status(HttpStatus.OK).json(new HealthDto("UP"));
      } else {
        ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(new HealthDto("DOWN"));
      }
    } catch (Exception e) {
      ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(Map.of("status", "DOWN", "error", e.getMessage()));
    }
  }
}
