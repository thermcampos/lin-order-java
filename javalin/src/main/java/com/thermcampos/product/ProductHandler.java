package com.thermcampos.product;

import java.util.Map;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ProductHandler {
  public void getProducts(Context ctx) {
    try {
      ctx.status(HttpStatus.OK).json(new ProductService().getProducts(ctx));
    } catch (Exception e) {
      ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("message", "Failed to list products", "error", e.getMessage()));
    }
  }
}
