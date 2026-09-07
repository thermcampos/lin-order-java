package com.thermcampos.product;

import com.thermcampos.db.DbKeys;
import java.util.List;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
  
  public List<Product> getProducts(Context ctx) {
    var ds = ctx.appData(DbKeys.DATA_SOURCE);
    var repository = new ProductRepository(ds);
    List<Product> products = repository.findAllProducts();
    logger.info("Found {} products in the database", products.size());
    return products;
  }
}
