package com.generated.ldesportsbar.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldesportsbar.enums.NotificationType;
import com.generated.ldesportsbar.model.Notification;
import com.generated.ldesportsbar.service.NotificationService;

@RestController
@RequestMapping({"/api/notifications", "/notifications"})
public class NotificationController {
  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping
  public ResponseEntity<List<Notification>> getNotifications(
      @RequestParam String role,
      @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {
    if (unreadOnly) {
      return ResponseEntity.ok(notificationService.getUnreadNotificationsByRole(role));
    }
    return ResponseEntity.ok(notificationService.getNotificationsByRole(role));
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable NotificationType type) {
    return ResponseEntity.ok(notificationService.getNotificationsByType(type));
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getUnreadCount(@RequestParam String role) {
    return ResponseEntity.ok(notificationService.getUnreadCount(role));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
    return notificationService.getNotificationById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/read")
  public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
    notificationService.markAsRead(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/read-all")
  public ResponseEntity<Void> markAllAsRead(@RequestParam String role) {
    notificationService.markAllAsRead(role);
    return ResponseEntity.ok().build();
  }
}
