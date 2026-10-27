package com.thermcampos;

import com.thermcampos.config.AppConfig;
import com.thermcampos.config.DbConfig;
import com.thermcampos.mapper.CustomJsonMapper;
import com.thermcampos.config.PropertiesLoadConfig;
import com.thermcampos.health.HealthResources;
import com.thermcampos.logger.AppLogger;
import com.thermcampos.product.ProductResources;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {
        var props = new PropertiesLoadConfig("application.properties");
        var logger = new AppLogger(App.class, props);

        logger.info("Starting app");

        var dbConfig = new DbConfig(props);
        var dataSource = dbConfig.create();
        dbConfig.migrateFlyway(dataSource);

        var app = Javalin.create(config -> {
            // Configs
            config.startup.showJavalinBanner = true;
            
            // JSON Mappers
            config.jsonMapper(new CustomJsonMapper());
            
            // Holders
            config.appData(AppConfig.PROPERTIES, props);
            config.appData(AppConfig.DATA_SOURCE, dataSource);

            // Events
            config.events.serverStopping(dataSource::close); 
            
            // Routes
            HealthResources.register(config);
            ProductResources.register(config);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down app");
            app.stop();
        }));

        app.start(props.getInt("order.server.port", 8080));
    }
}

