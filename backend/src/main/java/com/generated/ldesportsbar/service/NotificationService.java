package com.generated.ldesportsbar.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.generated.ldesportsbar.enums.NotificationType;
import com.generated.ldesportsbar.exception.ApiException;
import com.generated.ldesportsbar.mapper.NotificationMapper;
import com.generated.ldesportsbar.model.Notification;

@Service
public class NotificationService {
  private final NotificationMapper notificationMapper;

  public NotificationService(NotificationMapper notificationMapper) {
    this.notificationMapper = notificationMapper;
  }

  public List<Notification> getNotificationsByRole(String role) {
    return notificationMapper.findByRecipientRole(role);
  }

  public List<Notification> getUnreadNotificationsByRole(String role) {
    return notificationMapper.findUnreadByRecipientRole(role);
  }

  public List<Notification> getNotificationsByType(NotificationType type) {
    return notificationMapper.findByType(type);
  }

  public Optional<Notification> getNotificationById(Long id) {
    return notificationMapper.findById(id);
  }

  public long getUnreadCount(String role) {
    return notificationMapper.countUnreadByRecipientRole(role);
  }

  @Transactional
  public Notification createNotification(Notification notification) {
    notificationMapper.insert(notification);
    return notificationMapper.findById(notification.getId()).orElseThrow();
  }

  @Transactional
  public void markAsRead(Long id) {
    notificationMapper.findById(id)
        .orElseThrow(() -> new ApiException("通知不存在: " + id));
    notificationMapper.markAsRead(id);
  }

  @Transactional
  public void markAllAsRead(String role) {
    notificationMapper.markAllAsRead(role);
  }
}
