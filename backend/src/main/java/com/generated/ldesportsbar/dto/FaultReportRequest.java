package com.generated.ldesportsbar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.generated.ldesportsbar.enums.FaultType;
import com.generated.ldesportsbar.enums.Priority;

public class FaultReportRequest {
  @NotNull(message = "机位ID不能为空")
  private Long seatId;

  @NotNull(message = "故障类型不能为空")
  private FaultType faultType;

  @NotBlank(message = "故障描述不能为空")
  private String description;

  @NotBlank(message = "上报人不能为空")
  private String reporterName;

  private Priority priority = Priority.MEDIUM;

  private Integer timeoutMinutes = 60;

  public Long getSeatId() { return seatId; }
  public void setSeatId(Long seatId) { this.seatId = seatId; }
  public FaultType getFaultType() { return faultType; }
  public void setFaultType(FaultType faultType) { this.faultType = faultType; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getReporterName() { return reporterName; }
  public void setReporterName(String reporterName) { this.reporterName = reporterName; }
  public Priority getPriority() { return priority; }
  public void setPriority(Priority priority) { this.priority = priority; }
  public Integer getTimeoutMinutes() { return timeoutMinutes; }
  public void setTimeoutMinutes(Integer timeoutMinutes) { this.timeoutMinutes = timeoutMinutes; }
}
