package com.thermcampos.product;

import com.thermcampos.mapper.JsonUtil;
import java.util.Map;

public record ProductRequest (
    String name,
    Integer priceCents,
    Integer quantity) {

  public static ProductRequest fromMap(Map<String, Object> map) {
    if (map == null) {
      return null;
    }
    return new ProductRequest(
        JsonUtil.asString(map.get("name")),
        JsonUtil.asInteger(map.get("priceCents")),
        JsonUtil.asInteger(map.get("quantity")));
  }

  public static ProductRequest fromJson(String json) {
    return fromMap(JsonUtil.fromJson(json));
  }
}

