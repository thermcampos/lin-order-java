package com.thermcampos.health;

import com.thermcampos.mapper.JsonBaseDto;

public class HealthDto implements JsonBaseDto {
  private String status;

  public HealthDto(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public String toJson() {
    return "{\"status\":\"" + status + "\"}";
  }
}
