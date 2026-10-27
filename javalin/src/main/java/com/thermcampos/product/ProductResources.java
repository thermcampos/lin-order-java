package com.thermcampos.product;

import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import java.util.List;
import java.util.Map;

public class ProductResources {

  public static void register(JavalinConfig c) {
    c.routes.get("/products", ctx -> {
      try {
        List<Product> list = new ProductService().getProducts(ctx);

          ctx.status(HttpStatus.OK)
              .json(list);
      } catch (Exception e) {
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .json(Map.of("message", "Failed to list products",
                  "error", e.getMessage()));
      }
    });
  }
}
