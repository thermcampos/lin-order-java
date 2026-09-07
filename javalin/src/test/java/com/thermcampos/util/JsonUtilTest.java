package com.thermcampos.util;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonUtilTest {
  
  @Test 
  public void toJsonTest_buildSimpleObject_shouldSucceed() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "OK");
    String json = JsonUtil.toJson(response);
    System.out.println(json);
    Assertions.assertEquals("{\"status\":\"O\"}", json);
  }
}
