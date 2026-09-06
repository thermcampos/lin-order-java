package com.thermcampos;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thermcampos.health.HealthCheckHandler;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class.getName());

    public static void main( String[] args ) {
        logger.info("Starting app");
        Javalin.create(config -> {
            config.startup.showJavalinBanner = true;
            config.routes.get("/", ctx -> {
                ctx.status(200);
                ctx.result("Hello Ricardo");
            });
            config.routes.get("/health", HealthCheckHandler::getHealthHandler);
        }).start(8080);
    }
}
