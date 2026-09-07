package com.thermcampos.health;

import com.thermcampos.util.JsonUtil;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class HealthCheckHandler {
    
  public void getHealth(Context ctx, HikariDataSource ds) {
    try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
      if (conn.isValid(3)) {
        stmt.execute("SELECT 1");
        ctx.addHeader(ContentType.APPLICATION_JSON.name(), ContentType.APPLICATION_JSON.getMimeType());
        ctx.status(HttpStatus.OK);
        ctx.json(new HealthDto("UP"));
      } else {
        ctx.status(HttpStatus.SERVICE_UNAVAILABLE);
        ctx.json(new HealthDto("DOWN"));
      }
    } catch (Exception e) {
      ctx.status(HttpStatus.SERVICE_UNAVAILABLE);
      ctx.result(JsonUtil.toJson(Map.of("status", "DOWN", "error", e.getMessage())));
    }
  }
}
