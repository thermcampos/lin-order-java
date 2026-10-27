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
}

