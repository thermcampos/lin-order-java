package com.thermcampos.config;

import com.thermcampos.mapper.CustomJsonMapper;
import io.javalin.config.JavalinConfig;

public class AppConfig {
  public static void makeConfig(JavalinConfig config) {
    config.startup.showJavalinBanner = true;
    config.jsonMapper(new CustomJsonMapper());
  }
}
