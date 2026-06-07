package com.generated.ldesportsbar.model;

public class MaintenanceStats {
  private long pendingCount;
  private long processingCount;
  private long completedToday;
  private long timeoutCount;

  public MaintenanceStats() {}

  public MaintenanceStats(long pendingCount, long processingCount, long completedToday, long timeoutCount) {
    this.pendingCount = pendingCount;
    this.processingCount = processingCount;
    this.completedToday = completedToday;
    this.timeoutCount = timeoutCount;
  }

  public long getPendingCount() { return pendingCount; }
  public long getProcessingCount() { return processingCount; }
  public long getCompletedToday() { return completedToday; }
  public long getTimeoutCount() { return timeoutCount; }

  public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }
  public void setProcessingCount(long processingCount) { this.processingCount = processingCount; }
  public void setCompletedToday(long completedToday) { this.completedToday = completedToday; }
  public void setTimeoutCount(long timeoutCount) { this.timeoutCount = timeoutCount; }
}
