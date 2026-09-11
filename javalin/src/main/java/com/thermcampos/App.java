package com.thermcampos;

import com.thermcampos.config.AppConfig;
import com.thermcampos.config.DbConfig;
import com.thermcampos.mapper.CustomJsonMapper;
import com.thermcampos.config.PropertiesLoadConfig;
import com.thermcampos.health.HealthRoutes;
import com.thermcampos.product.ProductRoutes;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) {
        logger.info("Starting app");

        var props = new PropertiesLoadConfig("application.properties");
        var dbConfig = new DbConfig(props);
        HikariDataSource dataSource = dbConfig.create();
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
            HealthRoutes.register(config);
            ProductRoutes.register(config);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down app");
            app.stop();
        }));

        app.start(props.getInt("order.server.port", 8080));
    }
}

