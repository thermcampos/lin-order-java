package com.thermcampos.config;

import io.javalin.config.JavalinConfig;

public class AppConfig {
  public static void makeConfig(JavalinConfig config) {
    config.startup.showJavalinBanner = true;
  }
}
