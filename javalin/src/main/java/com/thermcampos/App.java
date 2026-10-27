package com.thermcampos;

import com.thermcampos.config.DbConfig;
import com.thermcampos.mapper.CustomJsonMapper;
import com.thermcampos.config.PropertiesLoadConfig;
import com.thermcampos.health.HealthResources;
import com.thermcampos.health.HealthService;
import com.thermcampos.product.ProductRepository;
import com.thermcampos.product.ProductResources;
import com.thermcampos.product.ProductService;
import com.thermcampos.web.ErrorHandlers;
import com.thermcampos.web.RequestLogging;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        var props = new PropertiesLoadConfig("application.properties");

        log.info("Starting app");

        var dbConfig = new DbConfig(props);
        var dataSource = dbConfig.create();
        dbConfig.migrateFlyway(dataSource);

        // Wiring
        var healthService = new HealthService(dataSource);
        var productService = new ProductService(new ProductRepository(dataSource));

        var app = Javalin.create(config -> {
            // Configs
            config.startup.showJavalinBanner = false;

            // JSON Mappers
            config.jsonMapper(new CustomJsonMapper());

            // Events
            config.events.serverStopping(dataSource::close);

            // Cross-cutting: request-id + access log, error responses
            RequestLogging.register(config);
            ErrorHandlers.register(config);

            // Routes
            HealthResources.register(config, healthService);
            ProductResources.register(config, productService);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down app");
            app.stop();
        }));

        app.start(props.getInt("order.server.port", 8080));
    }
}
