package com.thermcampos.product;

import com.thermcampos.validation.NotFoundException;
import com.thermcampos.validation.ValidationException;
import com.thermcampos.validation.ValidationUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository repository;

  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  public List<Product> getProducts() {
    List<Product> products = repository.findAllProducts();
    log.info("Found {} products in the database", products.size());

    return products;
  }

  public Product findById(Long id) {
    log.info("Searching product by id: {}", id);

    Product product = repository.findById(id);
    if (product == null) {
      log.info("Product not found for id: {}", id);
      throw new NotFoundException("product", "Product not found for id: " + id);
    }

    return product;
  }

  public Product createProduct(ProductRequest payload) {
    // Throws ValidationException, mapped to 400 in web/ErrorHandlers
    validateCreation(payload);

    var productCreated = repository.createProduct(payload);
    log.info("Product created. New ID: {}", productCreated.getId());

    return productCreated;
  }

  public Product patchProduct(Long id, ProductRequest payload) {
    // Throws ValidationException, mapped to 400 in web/ErrorHandlers
    validatePatch(payload);

    var product = repository.findById(id);
    if (product == null) {
      log.info("Product not found for id: {}", id);
      throw new NotFoundException("product", "Product not found for id: " + id);
    }

    // name
    String nameToSave = product.getName();
    if (payload.name() != null) {
      nameToSave = payload.name();
    }

    // price cents
    Integer priceToSave = product.getPriceCents();
    if (payload.priceCents() != null) {
      priceToSave = payload.priceCents();
    }

    // quantity
    Integer quantityToSave = product.getQuantity();
    if (payload.quantity() != null) {
      quantityToSave = payload.quantity();
    }

    var productToPatch = new Product(
        id,
        nameToSave,
        priceToSave,
        quantityToSave,
        null
    );

    var productPatched = repository.updateProduct(productToPatch);
    log.info("Product patched, id: {}", productPatched.getId());

    return productPatched;
  }

  public void deleteProduct(Long id) {
    var product = repository.findById(id);
    if (product == null) {
      log.info("Product not found for id: {}", id);
      throw new NotFoundException("product", "Product not found for id: " + id);
    }

    int rows = repository.deleteProduct(id);
    log.info("{} rows deleted for product id: {}", rows, id);
  }

  private void validatePatch(ProductRequest payload) {
    if (payload == null) {
      throw new ValidationException("body", "must not be empty");
    }

    // name - max size
    if (payload.name() != null) {
      ValidationUtil.maxSize("name", payload.name(), ValidationUtil.NAME_MAX_SIZE);
    }

    // price cents - negative
    if (payload.priceCents() != null) {
      ValidationUtil.notNegative("priceCents", payload.priceCents());
    }

    // quantity - negative
    if (payload.quantity() != null) {
      ValidationUtil.notNegative("quantity", payload.quantity());
    }
  }

  private void validateCreation(ProductRequest payload) {
    if (payload == null) {
      throw new ValidationException("body", "must not be empty");
    }

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
  }
}
