package com.thermcampos.product;

import com.thermcampos.config.AppConfig;
import com.thermcampos.logger.AppLogger;
import java.util.List;
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

    var repository = new ProductRepository(ds);

    var productCreated = repository.createProduct(payload);
    logger.info("Proeduct created. New ID: {}", productCreated.getId());

    return productCreated;
  }
}

