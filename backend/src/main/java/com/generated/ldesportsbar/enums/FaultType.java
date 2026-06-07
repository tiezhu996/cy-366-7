package com.generated.ldesportsbar.enums;

public enum FaultType {
  HARDWARE("硬件故障"),
  SOFTWARE("软件故障"),
  NETWORK("网络故障"),
  PERIPHERAL("外设故障"),
  POWER("电源故障"),
  OTHER("其他故障");

  private final String description;

  FaultType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
