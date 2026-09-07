package com.thermcampos.db;

import com.zaxxer.hikari.HikariDataSource;
import io.javalin.config.Key;

public class DbKeys {
  public static final Key<HikariDataSource> DATA_SOURCE = new Key<>("data-source");
}
