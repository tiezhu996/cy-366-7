package com.generated.ldesportsbar.enums;

public enum OrderStatus {
  PENDING("待派工"),
  ASSIGNED("已派工"),
  PROCESSING("处理中"),
  COMPLETED("已完成"),
  CANCELLED("已取消");

  private final String description;

  OrderStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
