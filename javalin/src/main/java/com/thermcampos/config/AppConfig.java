package com.thermcampos.config;

import com.zaxxer.hikari.HikariDataSource;
import io.javalin.config.Key;

public class AppConfig {

    public static final Key<HikariDataSource> DATA_SOURCE = new Key<>("data-source");
    
    public static final Key<PropertiesLoadConfig> PROPERTIES = new Key<>("properties");
}

