package com.generated.ldesportsbar.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.generated.ldesportsbar.service.MaintenanceOrderService;

@Component
public class MaintenanceScheduler {
  private static final Logger logger = LoggerFactory.getLogger(MaintenanceScheduler.class);
  private final MaintenanceOrderService orderService;

  public MaintenanceScheduler(MaintenanceOrderService orderService) {
    this.orderService = orderService;
  }

  @Scheduled(fixedRate = 60000)
  public void checkTimeoutOrders() {
    try {
      int notifiedCount = orderService.checkAndNotifyTimeoutOrders();
      if (notifiedCount > 0) {
        logger.info("超时工单检测完成，已通知 {} 个超时工单给店长", notifiedCount);
      }
    } catch (Exception e) {
      logger.error("超时工单检测失败", e);
    }
  }
}
