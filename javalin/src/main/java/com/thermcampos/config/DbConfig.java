package com.thermcampos.config;

import com.thermcampos.db.DbKeys;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.config.JavalinConfig;

public class DbConfig {

  public static void makeConfig(JavalinConfig c, HikariDataSource ds) {
    c.events.serverStopping(ds::close);
    c.appData(DbKeys.DATA_SOURCE, ds);
  }

  public static HikariDataSource create() {
    var config = new HikariConfig();
    config.setJdbcUrl(env("DB_URL", "jdbc:postgresql://localhost:5432/order"));
    config.setUsername(env("DB_USER", "order"));
    config.setPassword(env("DB_PASSWORD", "order"));
    config.setMaximumPoolSize(Integer.parseInt(env("DB_MAX_POOL_SIZE", "10")));
    config.setPoolName("linjava-pool");
    return new HikariDataSource(config);
  }

  private static String env(String key, String fallback) {
    var value = System.getenv(key);
    return (value == null || value.isBlank()) ? fallback : value;
  }
}
