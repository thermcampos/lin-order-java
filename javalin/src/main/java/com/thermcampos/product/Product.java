package com.thermcampos.product;

import com.thermcampos.mapper.JsonBaseDto;
import java.time.Instant;

public class Product implements JsonBaseDto {
  private Long id;
  private String name;
  private Integer priceCents;
  private Integer quantity;
  private Instant createdAt;

  public Product(Long id, String name, Integer priceCents, Integer quantity, Instant createdAt) {
    this.id = id;
    this.name = name;
    this.priceCents = priceCents;
    this.quantity = quantity;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getPriceCents() {
    return priceCents;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toJson() {
    return "{"
        + "\"id\":" + id + ","
        + "\"name\":\"" + name + "\","
        + "\"priceCents\":" + priceCents + ","
        + "\"quantity\":" + quantity + ","
        + "\"createdAt\":\"" + createdAt + "\""
        + "}";
  }
}
