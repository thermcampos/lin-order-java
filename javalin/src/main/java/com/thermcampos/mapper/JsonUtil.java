package com.thermcampos.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
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

  public static Map<String, Object> fromJson(String json) {
    Parser parser = new Parser(json);
    Map<String, Object> result = parser.parseObject();
    parser.skipWhitespace();
    if (!parser.isAtEnd()) {
      throw new IllegalArgumentException("Unexpected trailing content at position " + parser.pos);
    }
    return result;
  }

  public static String toString(Object obj) {
    return obj.toString();
  }

  public static String asString(Object value) {
    return value == null ? null : (String) value;
  }

  public static Integer asInteger(Object value) {
    return value == null ? null : ((Number) value).intValue();
  }

  public static Long asLong(Object value) {
    return value == null ? null : ((Number) value).longValue();
  }

  public static Boolean asBoolean(Object value) {
    return value == null ? null : (Boolean) value;
  }

  public static BigDecimal asBigDecimal(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Long longValue) {
      return BigDecimal.valueOf(longValue);
    }
    if (value instanceof Double doubleValue) {
      return BigDecimal.valueOf(doubleValue);
    }
    return new BigDecimal(value.toString());
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> asMap(Object value) {
    return value == null ? null : (Map<String, Object>) value;
  }

  public static String[] asStringArray(Object value) {
    if (value == null) {
      return null;
    }
    List<?> list = (List<?>) value;
    String[] result = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      result[i] = (String) list.get(i);
    }
    return result;
  }

  public static LocalDateTime asLocalDateTime(Object value) {
    String text = asString(value);
    if (text == null) {
      return null;
    }
    if (text.endsWith("Z")) {
      return LocalDateTime.ofInstant(Instant.parse(text), ZoneOffset.UTC);
    }
    return LocalDateTime.parse(text);
  }

  public static OffsetDateTime asOffsetDateTime(Object value) {
    String text = asString(value);
    if (text == null) {
      return null;
    }
    int length = text.length();
    boolean hasOffset = text.endsWith("Z")
        || (length > 6 && (text.charAt(length - 6) == '+' || text.charAt(length - 6) == '-'));
    if (hasOffset) {
      return OffsetDateTime.parse(text);
    }
    return LocalDateTime.parse(text).atOffset(ZoneOffset.UTC);
  }

  public static String quote(String s) {
    var sb = new StringBuilder(s.length() + 2).append('"');
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '"'  -> sb.append("\\\"");
        case '\\' -> sb.append("\\\\");
        case '\n' -> sb.append("\\n");
        case '\r' -> sb.append("\\r");
        case '\t' -> sb.append("\\t");
        case '\b' -> sb.append("\\b");
        case '\f' -> sb.append("\\f");
        default -> {
          if (c < 0x20) {
            sb.append(String.format("\\u%04x", (int) c));
          } else {
            sb.append(c);
          }
        }
      }
    }
    return sb.append('"').toString();
  }
}

