package com.thermcampos.product;

import com.thermcampos.validation.ValidationException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;

public class ProductResources {

  public static void register(JavalinConfig c, ProductService productService) {

    // Get - list products
    c.routes.get("/products", ctx -> {
      ctx.status(HttpStatus.OK)
          .json(productService.getProducts());
    });

    // Get By ID - return single product
    c.routes.get("/products/{id}", ctx -> {
      Long id = ctx.pathParamAsClass("id", Long.class)
          .check(v -> v > 0, "ID must be positive")
          .get();

      Product product = productService.findById(id);

      ctx.status(HttpStatus.OK)
          .json(product);
    });

    // Put - creates a product
    c.routes.put("/products", ctx -> {
      ProductRequest body;
      try {
        body = ctx.bodyAsClass(ProductRequest.class);
      } catch (IllegalArgumentException e) {
        // Malformed JSON
        throw new ValidationException("body", "failed to parse payload: " + e.getMessage());
      } catch (ClassCastException e) {
        // Valid JSON, but a field has the wrong type (e.g. "priceCents": "abc")
        throw new ValidationException("body", "a field has an invalid type");
      }

      ctx.status(HttpStatus.CREATED)
          .json(productService.createProduct(body));
    });

    // Patch - updates product
    c.routes.patch("/products/{id}", ctx -> {
      Long id = ctx.pathParamAsClass("id", Long.class)
          .check(v -> v > 0, "ID must be positive")
          .get();

      // Get the new body - to patch
      ProductRequest body;
      try {
        body = ctx.bodyAsClass(ProductRequest.class);
      } catch (IllegalArgumentException e) {
        // Malformed JSON
        throw new ValidationException("body", "failed to parse payload: " + e.getMessage());
      } catch (ClassCastException e) {
        // Valid JSON, but a field has the wrong type (e.g. "priceCents": "abc")
        throw new ValidationException("body", "a field has an invalid type");
      }

      ctx.status(HttpStatus.OK)
          .json(productService.patchProduct(id, body));
    });

    // Delete - delete a product
    c.routes.delete("/products/{id}", ctx -> {
      Long id = ctx.pathParamAsClass("id", Long.class)
          .check(v -> v > 0, "ID must be positive")
          .get();

      productService.deleteProduct(id);

      ctx.status(HttpStatus.OK);
    });
  }
}
