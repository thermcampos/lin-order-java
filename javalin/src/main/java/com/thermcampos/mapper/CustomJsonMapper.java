package com.thermcampos.mapper;

import com.thermcampos.util.JsonUtil;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;

public class CustomJsonMapper implements JsonMapper {
  
  @Override
  public String toJsonString(Object obj, Type type) {
    if (obj instanceof JsonBaseDto) {
      return ((JsonBaseDto) obj).toJson();
    }
    return JsonUtil.toString(obj);
  }
}
