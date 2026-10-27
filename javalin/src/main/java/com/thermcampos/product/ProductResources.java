package com.thermcampos.product;

import com.thermcampos.config.AppConfig;
import com.thermcampos.logger.AppLogger;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.validation.ValidationException;
import java.util.List;
import java.util.Map;

public class ProductResources {

  public static void register(JavalinConfig c) {
    final var productService = new ProductService();

    // Get - list products
    c.routes.get("/products", ctx -> {
      try {
        List<Product> list = productService.getProducts(ctx);

        ctx.status(HttpStatus.OK)
            .json(list);
      } catch (Exception e) {
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .json(Map.of(
                  "message", "Failed to list products",
                  "error", e.getMessage()));
      }
    });

    // Get By ID - return single product
    c.routes.get("/products/{id}", ctx -> {
      try {
        var props = ctx.appData(AppConfig.PROPERTIES);
        var logger = new AppLogger(ProductResources.class, props);

        Long id = ctx.pathParamAsClass("id", Long.class)
            .check(v -> v > 0, "ID must be posititive")
            .get();

        logger.info("Searching product by id: {}", id);

        Product product = productService.findById(ctx, id);
        if (product == null) {
          logger.info("Product not found for id: {}", id);
          ctx.status(HttpStatus.NOT_FOUND)
              .json(Map.of("message", "Product not found"));
          return;
        }

        logger.info("Product from search: {}", product.toJson());

        ctx.status(HttpStatus.OK)
            .json(product);
      } catch (Exception e) {
        if (e instanceof ValidationException) {
          ctx.status(HttpStatus.BAD_REQUEST)
              .json(Map.of(
                  "message", "Failed to find product by id",
                  "error", e.getMessage()));
          return;
        }
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .json(Map.of(
                  "message", "Failed to find product by id",
                  "error", e.getMessage()));
      }
    });

    // Put - creates a product
    c.routes.put("/products", ctx -> {
      try {
        var props = ctx.appData(AppConfig.PROPERTIES);
        var logger = new AppLogger(ProductResources.class, props);
        var body = ctx.bodyAsClass(ProductRequest.class);
        logger.info("Body received: {}", body);

        Product product = productService.createProduct(ctx, body);
        logger.info("Product created: {}", product.toJson());

        ctx.status(HttpStatus.CREATED)
            .json(product);
      } catch (Exception e) {
        if (e instanceof IllegalArgumentException) {
          ctx.status(HttpStatus.BAD_REQUEST)
              .json(Map.of(
                  "message", "Failed to parse payload",
                  "error", e.getMessage()));
          return;
        }
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .json(Map.of(
                  "message", "Failed to create product",
                  "error", e.getMessage()));
      }
    });
  }
}
