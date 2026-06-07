package com.generated.ldesportsbar.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderCompleteRequest {
  @NotBlank(message = "处理结果不能为空")
  private String processResult;

  public String getProcessResult() { return processResult; }
  public void setProcessResult(String processResult) { this.processResult = processResult; }
}
