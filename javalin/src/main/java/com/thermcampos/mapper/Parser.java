package com.thermcampos.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class Parser {
  private final String json;
  int pos;

  Parser(String json) {
    this.json = json;
    this.pos = 0;
  }

  boolean isAtEnd() {
    return pos >= json.length();
  }

  void skipWhitespace() {
    while (!isAtEnd()) {
      char c = json.charAt(pos);
      if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
        pos++;
      } else {
        break;
      }
    }
  }

  private char expect(char expected) {
    skipWhitespace();
    if (isAtEnd() || json.charAt(pos) != expected) {
      throw new IllegalArgumentException(
          "Expected '" + expected + "' at position " + pos + " in: " + json);
    }
    return json.charAt(pos++);
  }

  Map<String, Object> parseObject() {
    expect('{');
    Map<String, Object> map = new LinkedHashMap<>();
    skipWhitespace();
    if (!isAtEnd() && json.charAt(pos) == '}') {
      pos++;
      return map;
    }
    while (true) {
      String key = parseString();
      expect(':');
      map.put(key, parseValue());
      skipWhitespace();
      if (isAtEnd()) {
        throw new IllegalArgumentException("Unterminated object in: " + json);
      }
      char c = json.charAt(pos++);
      if (c == '}') {
        return map;
      }
      if (c != ',') {
        throw new IllegalArgumentException(
            "Expected ',' or '}' at position " + (pos - 1) + " in: " + json);
      }
    }
  }

  private List<Object> parseArray() {
    expect('[');
    List<Object> list = new ArrayList<>();
    skipWhitespace();
    if (!isAtEnd() && json.charAt(pos) == ']') {
      pos++;
      return list;
    }
    while (true) {
      list.add(parseValue());
      skipWhitespace();
      if (isAtEnd()) {
        throw new IllegalArgumentException("Unterminated array in: " + json);
      }
      char c = json.charAt(pos++);
      if (c == ']') {
        return list;
      }
      if (c != ',') {
        throw new IllegalArgumentException(
            "Expected ',' or ']' at position " + (pos - 1) + " in: " + json);
      }
    }
  }

  private Object parseValue() {
    skipWhitespace();
    if (isAtEnd()) {
      throw new IllegalArgumentException("Unexpected end of input in: " + json);
    }
    char c = json.charAt(pos);
    return switch (c) {
      case '{' -> parseObject();
      case '[' -> parseArray();
      case '"' -> parseString();
      case 't' -> parseLiteral("true", Boolean.TRUE);
      case 'f' -> parseLiteral("false", Boolean.FALSE);
      case 'n' -> parseLiteral("null", null);
      default -> parseNumber();
    };
  }

  private Object parseLiteral(String literal, Object value) {
    if (json.startsWith(literal, pos)) {
      pos += literal.length();
      return value;
    }
    throw new IllegalArgumentException("Invalid literal at position " + pos + " in: " + json);
  }

  private String parseString() {
    expect('"');
    StringBuilder sb = new StringBuilder();
    while (!isAtEnd()) {
      char c = json.charAt(pos++);
      if (c == '"') {
        return sb.toString();
      }
      if (c == '\\') {
        if (isAtEnd()) {
          break;
        }
        char esc = json.charAt(pos++);
        switch (esc) {
          case '"' -> sb.append('"');
          case '\\' -> sb.append('\\');
          case '/' -> sb.append('/');
          case 'b' -> sb.append('\b');
          case 'f' -> sb.append('\f');
          case 'n' -> sb.append('\n');
          case 'r' -> sb.append('\r');
          case 't' -> sb.append('\t');
          case 'u' -> sb.append(parseUnicode());
          default -> throw new IllegalArgumentException(
              "Invalid escape '\\" + esc + "' at position " + (pos - 1) + " in: " + json);
        }
      } else {
        sb.append(c);
      }
    }
    throw new IllegalArgumentException("Unterminated string in: " + json);
  }

  private char parseUnicode() {
    if (pos + 4 > json.length()) {
      throw new IllegalArgumentException("Invalid unicode escape at position " + pos + " in: " + json);
    }
    int code = Integer.parseInt(json.substring(pos, pos + 4), 16);
    pos += 4;
    return (char) code;
  }

  private Number parseNumber() {
    int start = pos;
    if (!isAtEnd() && json.charAt(pos) == '-') {
      pos++;
    }
    boolean isDecimal = false;
    while (!isAtEnd()) {
      char c = json.charAt(pos);
      if (c >= '0' && c <= '9') {
        pos++;
      } else if (c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
        isDecimal = true;
        pos++;
      } else {
        break;
      }
    }
    if (start == pos) {
      throw new IllegalArgumentException("Invalid value at position " + pos + " in: " + json);
    }
    String number = json.substring(start, pos);
    if (isDecimal) {
      return Double.parseDouble(number);
    }
    return Long.parseLong(number);
  }
}

