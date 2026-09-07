package com.thermcampos.product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;

public class ProductRepository {
  
  private final HikariDataSource ds;

  public ProductRepository(HikariDataSource ds) {
    this.ds = ds;
  }

  public List<Product> findAllProducts() {
    String sql = "SELECT id, name, price_in_cents, quantity, created_at FROM products";

    try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
      List<Product> products = new ArrayList<>();

      try (ResultSet rs = stmt.executeQuery(sql)) {  
        while (rs.next()) {
          Long id = rs.getLong("id");
          String name = rs.getString("name");
          Integer priceCents = rs.getInt("price_in_cents");
          Integer quantity = rs.getInt("quantity");
          Instant createdAt = rs.getObject("created_at", OffsetDateTime.class).toInstant();
          products.add(new Product(id, name, priceCents, quantity, createdAt));
        }
        return products;
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to retrieve products from the database: " + e.getMessage(), e);
    }
  }
}
