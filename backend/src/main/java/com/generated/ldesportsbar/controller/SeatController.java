package com.generated.ldesportsbar.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldesportsbar.enums.SeatStatus;
import com.generated.ldesportsbar.model.Seat;
import com.generated.ldesportsbar.service.SeatService;

@RestController
@RequestMapping({"/api/seats", "/seats"})
public class SeatController {
  private final SeatService seatService;

  public SeatController(SeatService seatService) {
    this.seatService = seatService;
  }

  @GetMapping
  public ResponseEntity<List<Seat>> getAllSeats(
      @RequestParam(required = false) SeatStatus status,
      @RequestParam(required = false) String area) {
    if (status != null) {
      return ResponseEntity.ok(seatService.getSeatsByStatus(status));
    }
    if (area != null && !area.isEmpty()) {
      return ResponseEntity.ok(seatService.getSeatsByArea(area));
    }
    return ResponseEntity.ok(seatService.getAllSeats());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
    return seatService.getSeatById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/code/{seatCode}")
  public ResponseEntity<Seat> getSeatByCode(@PathVariable String seatCode) {
    return seatService.getSeatByCode(seatCode)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<Seat> updateStatus(@PathVariable Long id, @RequestParam SeatStatus status) {
    return ResponseEntity.ok(seatService.updateSeatStatus(id, status));
  }
}
