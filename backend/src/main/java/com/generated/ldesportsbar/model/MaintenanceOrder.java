package com.generated.ldesportsbar.model;

import java.time.LocalDateTime;
import com.generated.ldesportsbar.enums.FaultType;
import com.generated.ldesportsbar.enums.OrderStatus;
import com.generated.ldesportsbar.enums.Priority;

public class MaintenanceOrder {
  private Long id;
  private String orderNo;
  private Long seatId;
  private String seatCode;
  private String areaName;
  private FaultType faultType;
  private String description;
  private String reporterName;
  private String assigneeGroup;
  private String assigneeName;
  private OrderStatus orderStatus;
  private Priority priority;
  private String processResult;
  private Integer timeoutMinutes;
  private Boolean notifiedTimeout;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime assignedAt;
  private LocalDateTime completedAt;

  public MaintenanceOrder() {}

  public MaintenanceOrder(Long id, String orderNo, Long seatId, String seatCode, String areaName,
      FaultType faultType, String description, String reporterName, String assigneeGroup,
      String assigneeName, OrderStatus orderStatus, Priority priority, String processResult,
      Integer timeoutMinutes, Boolean notifiedTimeout, LocalDateTime createdAt, LocalDateTime updatedAt,
      LocalDateTime assignedAt, LocalDateTime completedAt) {
    this.id = id;
    this.orderNo = orderNo;
    this.seatId = seatId;
    this.seatCode = seatCode;
    this.areaName = areaName;
    this.faultType = faultType;
    this.description = description;
    this.reporterName = reporterName;
    this.assigneeGroup = assigneeGroup;
    this.assigneeName = assigneeName;
    this.orderStatus = orderStatus;
    this.priority = priority;
    this.processResult = processResult;
    this.timeoutMinutes = timeoutMinutes;
    this.notifiedTimeout = notifiedTimeout;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.assignedAt = assignedAt;
    this.completedAt = completedAt;
  }

  public Long getId() { return id; }
  public String getOrderNo() { return orderNo; }
  public Long getSeatId() { return seatId; }
  public String getSeatCode() { return seatCode; }
  public String getAreaName() { return areaName; }
  public FaultType getFaultType() { return faultType; }
  public String getDescription() { return description; }
  public String getReporterName() { return reporterName; }
  public String getAssigneeGroup() { return assigneeGroup; }
  public String getAssigneeName() { return assigneeName; }
  public OrderStatus getOrderStatus() { return orderStatus; }
  public Priority getPriority() { return priority; }
  public String getProcessResult() { return processResult; }
  public Integer getTimeoutMinutes() { return timeoutMinutes; }
  public Boolean getNotifiedTimeout() { return notifiedTimeout; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public LocalDateTime getAssignedAt() { return assignedAt; }
  public LocalDateTime getCompletedAt() { return completedAt; }

  public void setId(Long id) { this.id = id; }
  public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
  public void setSeatId(Long seatId) { this.seatId = seatId; }
  public void setSeatCode(String seatCode) { this.seatCode = seatCode; }
  public void setAreaName(String areaName) { this.areaName = areaName; }
  public void setFaultType(FaultType faultType) { this.faultType = faultType; }
  public void setDescription(String description) { this.description = description; }
  public void setReporterName(String reporterName) { this.reporterName = reporterName; }
  public void setAssigneeGroup(String assigneeGroup) { this.assigneeGroup = assigneeGroup; }
  public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }
  public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
  public void setPriority(Priority priority) { this.priority = priority; }
  public void setProcessResult(String processResult) { this.processResult = processResult; }
  public void setTimeoutMinutes(Integer timeoutMinutes) { this.timeoutMinutes = timeoutMinutes; }
  public void setNotifiedTimeout(Boolean notifiedTimeout) { this.notifiedTimeout = notifiedTimeout; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
  public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
  public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
