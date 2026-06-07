package com.generated.ldesportsbar.model;

import java.time.LocalDateTime;
import com.generated.ldesportsbar.enums.NotificationType;

public class Notification {
  private Long id;
  private String recipientRole;
  private String recipientName;
  private String title;
  private String content;
  private NotificationType notificationType;
  private Long relatedOrderId;
  private Boolean isRead;
  private LocalDateTime createdAt;

  public Notification() {}

  public Notification(Long id, String recipientRole, String recipientName, String title,
      String content, NotificationType notificationType, Long relatedOrderId, Boolean isRead,
      LocalDateTime createdAt) {
    this.id = id;
    this.recipientRole = recipientRole;
    this.recipientName = recipientName;
    this.title = title;
    this.content = content;
    this.notificationType = notificationType;
    this.relatedOrderId = relatedOrderId;
    this.isRead = isRead;
    this.createdAt = createdAt;
  }

  public Long getId() { return id; }
  public String getRecipientRole() { return recipientRole; }
  public String getRecipientName() { return recipientName; }
  public String getTitle() { return title; }
  public String getContent() { return content; }
  public NotificationType getNotificationType() { return notificationType; }
  public Long getRelatedOrderId() { return relatedOrderId; }
  public Boolean getIsRead() { return isRead; }
  public LocalDateTime getCreatedAt() { return createdAt; }

  public void setId(Long id) { this.id = id; }
  public void setRecipientRole(String recipientRole) { this.recipientRole = recipientRole; }
  public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
  public void setTitle(String title) { this.title = title; }
  public void setContent(String content) { this.content = content; }
  public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }
  public void setRelatedOrderId(Long relatedOrderId) { this.relatedOrderId = relatedOrderId; }
  public void setIsRead(Boolean isRead) { this.isRead = isRead; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
