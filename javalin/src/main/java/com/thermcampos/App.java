package com.thermcampos;

import com.thermcampos.config.AppConfig;
import com.thermcampos.config.DbConfig;
import com.thermcampos.routes.HealthRoutes;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class.getName());

    public static void main( String[] args ) {
        logger.info("Starting app");

        HikariDataSource dataSource = DbConfig.create();

        Javalin app = Javalin.create(config -> {
            // Config
            AppConfig.makeConfig(config);
            // Db
            DbConfig.makeConfig(config, dataSource);
            // Routes
            HealthRoutes.register(config, dataSource);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down app");
            app.stop();
        }));

        app.start(8080);
    }
}
