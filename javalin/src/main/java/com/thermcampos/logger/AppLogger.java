package com.thermcampos.logger;

import com.thermcampos.config.PropertiesLoadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {

  private final Logger logger;
  private final String template = "[%s] %s";

  public AppLogger(Class<?> className, PropertiesLoadConfig props) {
    this(className.getName(), props);
  }

  public AppLogger(String className, PropertiesLoadConfig props) {
    this.logger = LoggerFactory.getLogger(className);
  }

  public void info(String msg, Object... args) {
    this.logger.info(msg, args);
  }

  public void warn(String msg, Object... args) {
    this.logger.warn(msg, args);
  }

  public void error(String msg, Object... args) {
    this.logger.error(msg, args);
  }
}

