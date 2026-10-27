package com.thermcampos.product;

import com.thermcampos.config.AppConfig;
import com.thermcampos.logger.AppLogger;
import com.thermcampos.validation.ValidationException;
import com.thermcampos.validation.ValidationUtil;
import java.util.List;
import java.util.Optional;
import io.javalin.http.Context;

public class ProductService {
  
  public List<Product> getProducts(Context ctx) {
    var props = ctx.appData(AppConfig.PROPERTIES);
    var logger = new AppLogger(ProductService.class, props);
    var ds = ctx.appData(AppConfig.DATA_SOURCE);

    var repository = new ProductRepository(ds);
    List<Product> products = repository.findAllProducts();
    logger.info("Found {} products in the database", products.size());
    
    return products;
  }

  public Product findById(Context ctx, Long id) {
    var ds = ctx.appData(AppConfig.DATA_SOURCE);

    var repository = new ProductRepository(ds);
    Product product = repository.findById(id);

    return product;
  }

  public Product createProduct(Context ctx, ProductRequest payload) {
    var props = ctx.appData(AppConfig.PROPERTIES);
    var logger = new AppLogger(ProductService.class, props);
    var ds = ctx.appData(AppConfig.DATA_SOURCE);

    // Validation
    Optional<ValidationException> validation = validateCreation(payload);
    if (validation.isPresent()) {
      logger.info("Validation failed! {}: {}", validation.get().getErrorKey(),
          validation.get().getMessage());
      throw new RuntimeException(
          validation.get().getErrorKey() + ": " +
          validation.get().getMessage());
    }

    var repository = new ProductRepository(ds);

    var productCreated = repository.createProduct(payload);
    logger.info("Proeduct created. New ID: {}", productCreated.getId());

    return productCreated;
  }

  private Optional<ValidationException> validateCreation(ProductRequest payload) {
    try {
      // name - null, empty or missing
      ValidationUtil.notNullNorBlank("name", payload.name());

      // name - max size
      ValidationUtil.maxSize("name", payload.name(), ValidationUtil.NAME_MAX_SIZE);

      // price cents - null or missing
      ValidationUtil.notNull("priceCents", payload.priceCents());
      
      // price cents - negative
      ValidationUtil.notNegative("priceCents", payload.priceCents());

      // quantity - null or missing
      ValidationUtil.notNull("quantity", payload.quantity());

      // quantity - negative
      ValidationUtil.notNegative("quantity", payload.quantity());
      
      return Optional.empty();
    } catch (ValidationException ex) {
      return Optional.of(ex);
    }
  }
}

