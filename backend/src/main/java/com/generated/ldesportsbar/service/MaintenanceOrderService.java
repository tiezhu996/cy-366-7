package com.generated.ldesportsbar.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.generated.ldesportsbar.dto.FaultReportRequest;
import com.generated.ldesportsbar.dto.OrderAssignRequest;
import com.generated.ldesportsbar.dto.OrderCompleteRequest;
import com.generated.ldesportsbar.dto.OrderProcessRequest;
import com.generated.ldesportsbar.enums.NotificationType;
import com.generated.ldesportsbar.enums.OrderStatus;
import com.generated.ldesportsbar.enums.SeatStatus;
import com.generated.ldesportsbar.exception.ApiException;
import com.generated.ldesportsbar.mapper.MaintenanceOrderMapper;
import com.generated.ldesportsbar.model.MaintenanceOrder;
import com.generated.ldesportsbar.model.MaintenanceStats;
import com.generated.ldesportsbar.model.Notification;
import com.generated.ldesportsbar.model.Seat;

@Service
public class MaintenanceOrderService {
  private final MaintenanceOrderMapper orderMapper;
  private final SeatService seatService;
  private final NotificationService notificationService;
  private final AtomicLong orderCounter = new AtomicLong(0);

  public MaintenanceOrderService(MaintenanceOrderMapper orderMapper,
      SeatService seatService, NotificationService notificationService) {
    this.orderMapper = orderMapper;
    this.seatService = seatService;
    this.notificationService = notificationService;
  }

  private String generateOrderNo() {
    String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long seq = orderCounter.incrementAndGet();
    return String.format("WO%s%04d", dateStr, seq);
  }

  public List<MaintenanceOrder> getAllOrders() {
    return orderMapper.findAll();
  }

  public List<MaintenanceOrder> getOrdersByStatus(OrderStatus status) {
    return orderMapper.findByStatus(status);
  }

  public List<MaintenanceOrder> getOrdersBySeatId(Long seatId) {
    return orderMapper.findBySeatId(seatId);
  }

  public List<MaintenanceOrder> getOrdersByAssigneeGroup(String group) {
    return orderMapper.findByAssigneeGroup(group);
  }

  public List<MaintenanceOrder> getTimeoutOrders() {
    return orderMapper.findTimeoutOrders(LocalDateTime.now());
  }

  public Optional<MaintenanceOrder> getOrderById(Long id) {
    return orderMapper.findById(id);
  }

  public Optional<MaintenanceOrder> getOrderByNo(String orderNo) {
    return orderMapper.findByOrderNo(orderNo);
  }

  public MaintenanceStats getStats() {
    return orderMapper.getStats();
  }

  @Transactional
  public MaintenanceOrder reportFault(FaultReportRequest request) {
    Seat seat = seatService.getSeatById(request.getSeatId())
        .orElseThrow(() -> new ApiException("机位不存在: " + request.getSeatId()));

    if (seat.getSeatStatus() == SeatStatus.MAINTENANCE) {
      throw new ApiException("机位已处于维修状态，请勿重复上报");
    }

    String orderNo = generateOrderNo();
    LocalDateTime now = LocalDateTime.now();

    MaintenanceOrder order = new MaintenanceOrder(
      null,
      orderNo,
      seat.getId(),
      seat.getSeatCode(),
      seat.getAreaName(),
      request.getFaultType(),
      request.getDescription(),
      request.getReporterName(),
      "技术组",
      null,
      OrderStatus.PENDING,
      request.getPriority(),
      null,
      request.getTimeoutMinutes(),
      false,
      now,
      now,
      null,
      null
    );

    orderMapper.insert(order);

    seatService.updateSeatStatus(seat.getId(), SeatStatus.MAINTENANCE);

    Notification techNotification = new Notification(
      null,
      "技术组",
      null,
      "新维修工单: " + orderNo,
      String.format("机位%s(%s)报%s故障: %s",
        seat.getSeatCode(), seat.getAreaName(),
        request.getFaultType().getDescription(),
        request.getDescription()),
      NotificationType.ORDER_CREATED,
      order.getId(),
      false,
      now
    );
    notificationService.createNotification(techNotification);

    MaintenanceOrder createdOrder = orderMapper.findById(order.getId()).orElseThrow();

    autoAssignToTechGroup(createdOrder.getId());

    return orderMapper.findById(order.getId()).orElseThrow();
  }

  @Transactional
  public MaintenanceOrder autoAssignToTechGroup(Long orderId) {
    MaintenanceOrder order = orderMapper.findById(orderId)
        .orElseThrow(() -> new ApiException("工单不存在: " + orderId));

    if (order.getOrderStatus() != OrderStatus.PENDING) {
      throw new ApiException("工单状态不允许派工");
    }

    LocalDateTime now = LocalDateTime.now();
    MaintenanceOrder updated = new MaintenanceOrder(
      order.getId(),
      order.getOrderNo(),
      order.getSeatId(),
      order.getSeatCode(),
      order.getAreaName(),
      order.getFaultType(),
      order.getDescription(),
      order.getReporterName(),
      "技术组",
      "技术组值班人员",
      OrderStatus.ASSIGNED,
      order.getPriority(),
      order.getProcessResult(),
      order.getTimeoutMinutes(),
      order.getNotifiedTimeout(),
      order.getCreatedAt(),
      now,
      now,
      order.getCompletedAt()
    );

    orderMapper.update(updated);

    Notification notification = new Notification(
      null,
      "技术组",
      "技术组值班人员",
      "工单已派工: " + order.getOrderNo(),
      String.format("请处理机位%s的%s故障", order.getSeatCode(), order.getFaultType().getDescription()),
      NotificationType.ORDER_ASSIGNED,
      order.getId(),
      false,
      now
    );
    notificationService.createNotification(notification);

    return orderMapper.findById(orderId).orElseThrow();
  }

  @Transactional
  public MaintenanceOrder assignOrder(Long orderId, OrderAssignRequest request) {
    MaintenanceOrder order = orderMapper.findById(orderId)
        .orElseThrow(() -> new ApiException("工单不存在: " + orderId));

    if (order.getOrderStatus() != OrderStatus.PENDING && order.getOrderStatus() != OrderStatus.ASSIGNED) {
      throw new ApiException("工单状态不允许派工");
    }

    LocalDateTime now = LocalDateTime.now();
    MaintenanceOrder updated = new MaintenanceOrder(
      order.getId(),
      order.getOrderNo(),
      order.getSeatId(),
      order.getSeatCode(),
      order.getAreaName(),
      order.getFaultType(),
      order.getDescription(),
      order.getReporterName(),
      request.getAssigneeGroup(),
      request.getAssigneeName(),
      OrderStatus.ASSIGNED,
      order.getPriority(),
      order.getProcessResult(),
      order.getTimeoutMinutes(),
      order.getNotifiedTimeout(),
      order.getCreatedAt(),
      now,
      now,
      order.getCompletedAt()
    );

    orderMapper.update(updated);

    Notification notification = new Notification(
      null,
      request.getAssigneeGroup(),
      request.getAssigneeName(),
      "工单已派工: " + order.getOrderNo(),
      String.format("请处理机位%s的%s故障", order.getSeatCode(), order.getFaultType().getDescription()),
      NotificationType.ORDER_ASSIGNED,
      order.getId(),
      false,
      now
    );
    notificationService.createNotification(notification);

    return orderMapper.findById(orderId).orElseThrow();
  }

  @Transactional
  public MaintenanceOrder startProcessing(Long orderId, OrderProcessRequest request) {
    MaintenanceOrder order = orderMapper.findById(orderId)
        .orElseThrow(() -> new ApiException("工单不存在: " + orderId));

    if (order.getOrderStatus() != OrderStatus.ASSIGNED) {
      throw new ApiException("工单状态不允许开始处理");
    }

    LocalDateTime now = LocalDateTime.now();
    MaintenanceOrder updated = new MaintenanceOrder(
      order.getId(),
      order.getOrderNo(),
      order.getSeatId(),
      order.getSeatCode(),
      order.getAreaName(),
      order.getFaultType(),
      order.getDescription(),
      order.getReporterName(),
      order.getAssigneeGroup(),
      request.getAssigneeName(),
      OrderStatus.PROCESSING,
      order.getPriority(),
      request.getProcessResult(),
      order.getTimeoutMinutes(),
      order.getNotifiedTimeout(),
      order.getCreatedAt(),
      now,
      order.getAssignedAt(),
      order.getCompletedAt()
    );

    orderMapper.update(updated);
    return orderMapper.findById(orderId).orElseThrow();
  }

  @Transactional
  public MaintenanceOrder completeOrder(Long orderId, OrderCompleteRequest request) {
    MaintenanceOrder order = orderMapper.findById(orderId)
        .orElseThrow(() -> new ApiException("工单不存在: " + orderId));

    if (order.getOrderStatus() != OrderStatus.PROCESSING && order.getOrderStatus() != OrderStatus.ASSIGNED) {
      throw new ApiException("工单状态不允许完成");
    }

    LocalDateTime now = LocalDateTime.now();
    MaintenanceOrder updated = new MaintenanceOrder(
      order.getId(),
      order.getOrderNo(),
      order.getSeatId(),
      order.getSeatCode(),
      order.getAreaName(),
      order.getFaultType(),
      order.getDescription(),
      order.getReporterName(),
      order.getAssigneeGroup(),
      order.getAssigneeName(),
      OrderStatus.COMPLETED,
      order.getPriority(),
      request.getProcessResult(),
      order.getTimeoutMinutes(),
      order.getNotifiedTimeout(),
      order.getCreatedAt(),
      now,
      order.getAssignedAt(),
      now
    );

    orderMapper.update(updated);

    seatService.updateSeatStatus(order.getSeatId(), SeatStatus.AVAILABLE);

    Notification notification = new Notification(
      null,
      "运营组",
      null,
      "工单已完成: " + order.getOrderNo(),
      String.format("机位%s已修复，处理结果: %s", order.getSeatCode(), request.getProcessResult()),
      NotificationType.ORDER_COMPLETED,
      order.getId(),
      false,
      now
    );
    notificationService.createNotification(notification);

    Notification seatRecovered = new Notification(
      null,
      "运营组",
      null,
      "机位恢复可用: " + order.getSeatCode(),
      String.format("机位%s已修复并恢复可用状态", order.getSeatCode()),
      NotificationType.SEAT_RECOVERED,
      order.getId(),
      false,
      now
    );
    notificationService.createNotification(seatRecovered);

    return orderMapper.findById(orderId).orElseThrow();
  }

  @Transactional
  public MaintenanceOrder cancelOrder(Long orderId) {
    MaintenanceOrder order = orderMapper.findById(orderId)
        .orElseThrow(() -> new ApiException("工单不存在: " + orderId));

    if (order.getOrderStatus() == OrderStatus.COMPLETED) {
      throw new ApiException("已完成的工单不能取消");
    }

    LocalDateTime now = LocalDateTime.now();
    MaintenanceOrder updated = new MaintenanceOrder(
      order.getId(),
      order.getOrderNo(),
      order.getSeatId(),
      order.getSeatCode(),
      order.getAreaName(),
      order.getFaultType(),
      order.getDescription(),
      order.getReporterName(),
      order.getAssigneeGroup(),
      order.getAssigneeName(),
      OrderStatus.CANCELLED,
      order.getPriority(),
      "工单已取消",
      order.getTimeoutMinutes(),
      order.getNotifiedTimeout(),
      order.getCreatedAt(),
      now,
      order.getAssignedAt(),
      now
    );

    orderMapper.update(updated);

    seatService.updateSeatStatus(order.getSeatId(), SeatStatus.AVAILABLE);

    return orderMapper.findById(orderId).orElseThrow();
  }

  @Transactional
  public int checkAndNotifyTimeoutOrders() {
    LocalDateTime now = LocalDateTime.now();
    List<MaintenanceOrder> timeoutOrders = orderMapper.findTimeoutNotNotified(now);

    if (timeoutOrders.isEmpty()) {
      return 0;
    }

    List<Long> orderIds = timeoutOrders.stream()
        .map(MaintenanceOrder::getId)
        .toList();

    orderMapper.markNotifiedTimeout(orderIds);

    for (MaintenanceOrder order : timeoutOrders) {
      Notification notification = new Notification(
        null,
        "店长",
        "店长",
        "工单超时提醒: " + order.getOrderNo(),
        String.format("机位%s的%s故障已超时%d分钟未处理，请督促技术组尽快处理！当前状态: %s",
          order.getSeatCode(),
          order.getFaultType().getDescription(),
          order.getTimeoutMinutes(),
          order.getOrderStatus().getDescription()),
        NotificationType.ORDER_TIMEOUT,
        order.getId(),
        false,
        now
      );
      notificationService.createNotification(notification);
    }

    return timeoutOrders.size();
  }
}
