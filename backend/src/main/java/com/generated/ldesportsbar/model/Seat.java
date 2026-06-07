package com.generated.ldesportsbar.model;

import java.time.LocalDateTime;
import com.generated.ldesportsbar.enums.SeatStatus;

public class Seat {
  private Long id;
  private String seatCode;
  private String areaName;
  private SeatStatus seatStatus;
  private String configInfo;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Seat() {}

  public Seat(Long id, String seatCode, String areaName, SeatStatus seatStatus, String configInfo,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.seatCode = seatCode;
    this.areaName = areaName;
    this.seatStatus = seatStatus;
    this.configInfo = configInfo;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() { return id; }
  public String getSeatCode() { return seatCode; }
  public String getAreaName() { return areaName; }
  public SeatStatus getSeatStatus() { return seatStatus; }
  public String getConfigInfo() { return configInfo; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }

  public void setId(Long id) { this.id = id; }
  public void setSeatCode(String seatCode) { this.seatCode = seatCode; }
  public void setAreaName(String areaName) { this.areaName = areaName; }
  public void setSeatStatus(SeatStatus seatStatus) { this.seatStatus = seatStatus; }
  public void setConfigInfo(String configInfo) { this.configInfo = configInfo; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
