package com.thermcampos.product;

import io.javalin.config.JavalinConfig;

public class ProductRoutes {
  public static void register(JavalinConfig c) {
    c.routes.get("/products", ctx -> new ProductHandler().getProducts(ctx));
  }
}
