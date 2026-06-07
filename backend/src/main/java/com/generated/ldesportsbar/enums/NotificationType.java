package com.generated.ldesportsbar.enums;

public enum NotificationType {
  ORDER_CREATED("工单创建"),
  ORDER_ASSIGNED("工单派工"),
  ORDER_TIMEOUT("工单超时"),
  ORDER_COMPLETED("工单完成"),
  SEAT_RECOVERED("机位恢复");

  private final String description;

  NotificationType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
