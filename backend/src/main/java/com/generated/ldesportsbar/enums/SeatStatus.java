package com.generated.ldesportsbar.enums;

public enum SeatStatus {
  AVAILABLE("空闲"),
  OCCUPIED("使用中"),
  MAINTENANCE("维修中"),
  RESERVED("已预约");

  private final String description;

  SeatStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
