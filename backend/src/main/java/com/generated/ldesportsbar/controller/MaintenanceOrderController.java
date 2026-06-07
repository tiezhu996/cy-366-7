package com.generated.ldesportsbar.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldesportsbar.dto.FaultReportRequest;
import com.generated.ldesportsbar.dto.OrderAssignRequest;
import com.generated.ldesportsbar.dto.OrderCompleteRequest;
import com.generated.ldesportsbar.dto.OrderProcessRequest;
import com.generated.ldesportsbar.enums.OrderStatus;
import com.generated.ldesportsbar.model.MaintenanceOrder;
import com.generated.ldesportsbar.model.MaintenanceStats;
import com.generated.ldesportsbar.service.MaintenanceOrderService;

@RestController
@RequestMapping({"/api/maintenance-orders", "/maintenance-orders"})
public class MaintenanceOrderController {
  private final MaintenanceOrderService orderService;

  public MaintenanceOrderController(MaintenanceOrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<MaintenanceOrder>> getAllOrders(
      @RequestParam(required = false) OrderStatus status,
      @RequestParam(required = false) Long seatId,
      @RequestParam(required = false) String group) {
    if (status != null) {
      return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }
    if (seatId != null) {
      return ResponseEntity.ok(orderService.getOrdersBySeatId(seatId));
    }
    if (group != null && !group.isEmpty()) {
      return ResponseEntity.ok(orderService.getOrdersByAssigneeGroup(group));
    }
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/stats")
  public ResponseEntity<MaintenanceStats> getStats() {
    return ResponseEntity.ok(orderService.getStats());
  }

  @GetMapping("/timeout")
  public ResponseEntity<List<MaintenanceOrder>> getTimeoutOrders() {
    return ResponseEntity.ok(orderService.getTimeoutOrders());
  }

  @GetMapping("/{id}")
  public ResponseEntity<MaintenanceOrder> getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/no/{orderNo}")
  public ResponseEntity<MaintenanceOrder> getOrderByNo(@PathVariable String orderNo) {
    return orderService.getOrderByNo(orderNo)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/report-fault")
  public ResponseEntity<MaintenanceOrder> reportFault(@Valid @RequestBody FaultReportRequest request) {
    MaintenanceOrder order = orderService.reportFault(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @PutMapping("/{id}/assign")
  public ResponseEntity<MaintenanceOrder> assignOrder(
      @PathVariable Long id,
      @Valid @RequestBody OrderAssignRequest request) {
    return ResponseEntity.ok(orderService.assignOrder(id, request));
  }

  @PutMapping("/{id}/start")
  public ResponseEntity<MaintenanceOrder> startProcessing(
      @PathVariable Long id,
      @Valid @RequestBody OrderProcessRequest request) {
    return ResponseEntity.ok(orderService.startProcessing(id, request));
  }

  @PutMapping("/{id}/complete")
  public ResponseEntity<MaintenanceOrder> completeOrder(
      @PathVariable Long id,
      @Valid @RequestBody OrderCompleteRequest request) {
    return ResponseEntity.ok(orderService.completeOrder(id, request));
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<MaintenanceOrder> cancelOrder(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.cancelOrder(id));
  }

  @PostMapping("/check-timeout")
  public ResponseEntity<Integer> checkTimeout() {
    int count = orderService.checkAndNotifyTimeoutOrders();
    return ResponseEntity.ok(count);
  }
}
