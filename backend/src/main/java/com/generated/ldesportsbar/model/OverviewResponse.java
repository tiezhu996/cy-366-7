package com.generated.ldesportsbar.model;

import java.util.List;

public class OverviewResponse {
  private String appName;
  private String appCode;
  private String description;
  private List<FeatureItem> features;
  private List<KpiItem> kpis;
  private List<OperationRecord> records;
  private MaintenanceStats maintenanceStats;
  private List<MaintenanceOrder> recentOrders;

  public OverviewResponse() {}

  public OverviewResponse(String appName, String appCode, String description,
      List<FeatureItem> features, List<KpiItem> kpis, List<OperationRecord> records,
      MaintenanceStats maintenanceStats, List<MaintenanceOrder> recentOrders) {
    this.appName = appName;
    this.appCode = appCode;
    this.description = description;
    this.features = features;
    this.kpis = kpis;
    this.records = records;
    this.maintenanceStats = maintenanceStats;
    this.recentOrders = recentOrders;
  }

  public String getAppName() { return appName; }
  public String getAppCode() { return appCode; }
  public String getDescription() { return description; }
  public List<FeatureItem> getFeatures() { return features; }
  public List<KpiItem> getKpis() { return kpis; }
  public List<OperationRecord> getRecords() { return records; }
  public MaintenanceStats getMaintenanceStats() { return maintenanceStats; }
  public List<MaintenanceOrder> getRecentOrders() { return recentOrders; }

  public void setAppName(String appName) { this.appName = appName; }
  public void setAppCode(String appCode) { this.appCode = appCode; }
  public void setDescription(String description) { this.description = description; }
  public void setFeatures(List<FeatureItem> features) { this.features = features; }
  public void setKpis(List<KpiItem> kpis) { this.kpis = kpis; }
  public void setRecords(List<OperationRecord> records) { this.records = records; }
  public void setMaintenanceStats(MaintenanceStats maintenanceStats) { this.maintenanceStats = maintenanceStats; }
  public void setRecentOrders(List<MaintenanceOrder> recentOrders) { this.recentOrders = recentOrders; }
}
