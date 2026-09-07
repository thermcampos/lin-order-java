package com.thermcampos;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thermcampos.config.AppConfig;
import com.thermcampos.routes.HealthRoutes;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class.getName());

    public static void main( String[] args ) {
        logger.info("Starting app");
        Javalin.create(config -> {
            // Config
            AppConfig.makeConfig(config);
            // Routes
            HealthRoutes.register(config);
        }).start(8080);
    }
}
