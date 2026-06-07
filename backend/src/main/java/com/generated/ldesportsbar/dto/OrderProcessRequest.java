package com.generated.ldesportsbar.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderProcessRequest {
  @NotBlank(message = "处理人不能为空")
  private String assigneeName;

  private String processResult;

  public String getAssigneeName() { return assigneeName; }
  public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }
  public String getProcessResult() { return processResult; }
  public void setProcessResult(String processResult) { this.processResult = processResult; }
}
