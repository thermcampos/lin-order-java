package com.thermcampos.mapper;

import com.thermcampos.product.ProductRequest;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJsonMapper implements JsonMapper {
  
  @Override
  public String toJsonString(Object obj, Type type) {
    if (obj == null) {
      return "null";
    }
    if (obj instanceof JsonBaseDto dto) {
      return dto.toJson();
    }
    if (obj instanceof Map<?, ?> map) {
      @SuppressWarnings("unchecked")
      var casted = (Map<String, Object>) map;
      return JsonUtil.toJson(casted);
    }
    if (obj instanceof List<?> list) {
      return list.stream()
          .map(item -> toJsonString(item, item == null ? Object.class : item.getClass()))
          .collect(Collectors.joining(",", "[", "]"));
    }
    if (obj instanceof String s) {
      return JsonUtil.quote(s);
    }
    if (obj instanceof Character c) {
      return JsonUtil.quote(Character.toString(c));
    }
    if (obj instanceof Boolean b) {
      return b.toString();
    }
    if (obj instanceof Number n) {
      if (n instanceof Double d && (d.isNaN() || d.isInfinite())
          || n instanceof Float f && (f.isNaN() || f.isInfinite())) {
        return "null";
      }
      return n.toString();
    }
    throw new UnsupportedOperationException("No JSON serialization defined for: " + obj.getClass() + " - implement JsonBaseDto or Map<String, Object>");
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T fromJsonString(String json, Type targetType) {
    if (targetType == ProductRequest.class) {
      return (T) ProductRequest.fromJson(json);
    }
    if (targetType == Map.class) {
      return (T) JsonUtil.fromJson(json);
    }
    throw new UnsupportedOperationException("No JSON deserialization defined for: " + targetType + " - add a fromMap factory and register it here");
  }
}

