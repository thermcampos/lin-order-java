package com.thermcampos.mapper;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonUtilTest {
  
  @Test 
  public void toJsonTest_buildFromMap_shouldSucceed() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "OK");
    String json = JsonUtil.toJson(response);
    Assertions.assertEquals("{\"status\":\"OK\"}", json);
  }

  @Test 
  public void toJsonTest_buildFromObjectNotImplement_shouldSucceed() {
    var obj = new TestWithout("John", "Doe");
    String json = JsonUtil.toString(obj);
    Assertions.assertEquals(obj.toString(), json);
  }

  @Test
  public void toJsonTest_buildFromObjectImplement_shouldSucceed() {
    var obj = new TestWithBaseDto("John", "Doe");
    String json = obj.toJson();
    Assertions.assertEquals("{\"firstname\":\"John\",\"lastname\":\"Doe\"}", json);
  }
}

class TestWithBaseDto implements JsonBaseDto {
  private String firstname;
  private String lastname;

  public TestWithBaseDto(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  @Override
  public String toJson() {
    return "{\"firstname\":\"" + firstname + "\",\"lastname\":\"" + lastname + "\"}";
  }
}

class TestWithout {
  private String firstname;
  private String lastname;

  public TestWithout(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  @Override
  public String toString() {
    return "TestWithout[firstname='" + firstname + "', lastname='" + lastname + "']";
  }
}
