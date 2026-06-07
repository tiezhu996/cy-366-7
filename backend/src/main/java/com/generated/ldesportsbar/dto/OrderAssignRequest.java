package com.generated.ldesportsbar.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderAssignRequest {
  @NotBlank(message = "处理人不能为空")
  private String assigneeName;

  private String assigneeGroup = "技术组";

  public String getAssigneeName() { return assigneeName; }
  public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }
  public String getAssigneeGroup() { return assigneeGroup; }
  public void setAssigneeGroup(String assigneeGroup) { this.assigneeGroup = assigneeGroup; }
}
