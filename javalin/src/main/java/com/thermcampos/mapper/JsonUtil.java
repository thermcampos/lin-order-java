package com.thermcampos.mapper;

import java.util.Map;

public class JsonUtil {
  public static String toJson(Map<String, Object> map) {
    StringBuilder json = new StringBuilder("{");
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      json.append("\"").append(entry.getKey()).append("\":");
      if (entry.getValue() instanceof String) {
        json.append("\"").append(entry.getValue()).append("\"");
      } else {
        json.append(entry.getValue());
      }
      json.append(",");
    }
    if (json.length() > 1) {
      json.setLength(json.length() - 1); // Remove trailing comma
    }
    json.append("}");
    return json.toString();
  }

  public static String toString(Object obj) {
    return obj.toString();
  }
}
