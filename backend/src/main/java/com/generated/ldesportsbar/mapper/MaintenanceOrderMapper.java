package com.generated.ldesportsbar.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.generated.ldesportsbar.enums.OrderStatus;
import com.generated.ldesportsbar.model.MaintenanceOrder;
import com.generated.ldesportsbar.model.MaintenanceStats;

@Mapper
public interface MaintenanceOrderMapper {
  List<MaintenanceOrder> findAll();
  List<MaintenanceOrder> findByStatus(@Param("status") OrderStatus status);
  List<MaintenanceOrder> findBySeatId(@Param("seatId") Long seatId);
  List<MaintenanceOrder> findByAssigneeGroup(@Param("assigneeGroup") String assigneeGroup);
  List<MaintenanceOrder> findTimeoutOrders(@Param("now") LocalDateTime now);
  List<MaintenanceOrder> findTimeoutNotNotified(@Param("now") LocalDateTime now);
  Optional<MaintenanceOrder> findById(@Param("id") Long id);
  Optional<MaintenanceOrder> findByOrderNo(@Param("orderNo") String orderNo);
  MaintenanceStats getStats();
  int insert(MaintenanceOrder order);
  int update(MaintenanceOrder order);
  int updateStatus(@Param("id") Long id, @Param("status") OrderStatus status, @Param("updatedAt") LocalDateTime updatedAt);
  int updateNotifiedTimeout(@Param("id") Long id, @Param("notified") Boolean notified);
  int markNotifiedTimeout(@Param("ids") List<Long> ids);
}
