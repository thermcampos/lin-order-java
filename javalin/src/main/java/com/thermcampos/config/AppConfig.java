package com.thermcampos.config;

import com.thermcampos.mapper.CustomJsonMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.config.JavalinConfig;
import io.javalin.config.Key;

public class AppConfig {

    public static final Key<HikariDataSource> DATA_SOURCE = new Key<>("data-source");
    
    public static final Key<PropertiesLoadConfig> PROPERTIES = new Key<>("properties");

    public static void makeConfig(JavalinConfig config) {
        config.startup.showJavalinBanner = true;
        config.jsonMapper(new CustomJsonMapper());
    }
}

