package com.generated.ldesportsbar.model;

public class OperationRecord {
  private String key;
  private String name;
  private String owner;
  private String status;
  private String metric;
  private String priority;

  public OperationRecord() {}

  public OperationRecord(String key, String name, String owner, String status, String metric, String priority) {
    this.key = key;
    this.name = name;
    this.owner = owner;
    this.status = status;
    this.metric = metric;
    this.priority = priority;
  }

  public String getKey() { return key; }
  public String getName() { return name; }
  public String getOwner() { return owner; }
  public String getStatus() { return status; }
  public String getMetric() { return metric; }
  public String getPriority() { return priority; }

  public void setKey(String key) { this.key = key; }
  public void setName(String name) { this.name = name; }
  public void setOwner(String owner) { this.owner = owner; }
  public void setStatus(String status) { this.status = status; }
  public void setMetric(String metric) { this.metric = metric; }
  public void setPriority(String priority) { this.priority = priority; }
}
