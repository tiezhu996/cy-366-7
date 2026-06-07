package com.generated.ldesportsbar.model;

public class KpiItem {
  private String label;
  private String value;
  private String trend;
  private String tone;

  public KpiItem() {}

  public KpiItem(String label, String value, String trend, String tone) {
    this.label = label;
    this.value = value;
    this.trend = trend;
    this.tone = tone;
  }

  public String getLabel() { return label; }
  public String getValue() { return value; }
  public String getTrend() { return trend; }
  public String getTone() { return tone; }

  public void setLabel(String label) { this.label = label; }
  public void setValue(String value) { this.value = value; }
  public void setTrend(String trend) { this.trend = trend; }
  public void setTone(String tone) { this.tone = tone; }
}
