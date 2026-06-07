package com.generated.ldesportsbar.model;

public class FeatureItem {
  private int id;
  private String title;
  private String description;
  private String status;
  private String metric;

  public FeatureItem() {}

  public FeatureItem(int id, String title, String description, String status, String metric) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.metric = metric;
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public String getStatus() { return status; }
  public String getMetric() { return metric; }

  public void setId(int id) { this.id = id; }
  public void setTitle(String title) { this.title = title; }
  public void setDescription(String description) { this.description = description; }
  public void setStatus(String status) { this.status = status; }
  public void setMetric(String metric) { this.metric = metric; }
}
