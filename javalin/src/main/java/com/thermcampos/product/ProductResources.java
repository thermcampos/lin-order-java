package com.thermcampos.product;

import com.thermcampos.config.AppConfig;
import com.thermcampos.logger.AppLogger;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
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
            .json(Map.of("message", "Failed to list products",
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
