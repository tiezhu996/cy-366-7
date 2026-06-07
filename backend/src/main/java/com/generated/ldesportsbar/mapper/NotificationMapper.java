package com.generated.ldesportsbar.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.generated.ldesportsbar.enums.NotificationType;
import com.generated.ldesportsbar.model.Notification;

@Mapper
public interface NotificationMapper {
  List<Notification> findByRecipientRole(@Param("role") String role);
  List<Notification> findUnreadByRecipientRole(@Param("role") String role);
  List<Notification> findByType(@Param("type") NotificationType type);
  Optional<Notification> findById(@Param("id") Long id);
  int insert(Notification notification);
  int markAsRead(@Param("id") Long id);
  int markAllAsRead(@Param("role") String role);
  long countUnreadByRecipientRole(@Param("role") String role);
}
