package com.generated.ldesportsbar.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.generated.ldesportsbar.enums.SeatStatus;
import com.generated.ldesportsbar.exception.ApiException;
import com.generated.ldesportsbar.mapper.SeatMapper;
import com.generated.ldesportsbar.model.Seat;

@Service
public class SeatService {
  private final SeatMapper seatMapper;

  public SeatService(SeatMapper seatMapper) {
    this.seatMapper = seatMapper;
  }

  public List<Seat> getAllSeats() {
    return seatMapper.findAll();
  }

  public List<Seat> getSeatsByStatus(SeatStatus status) {
    return seatMapper.findByStatus(status);
  }

  public List<Seat> getSeatsByArea(String areaName) {
    return seatMapper.findByArea(areaName);
  }

  public Optional<Seat> getSeatById(Long id) {
    return seatMapper.findById(id);
  }

  public Optional<Seat> getSeatByCode(String seatCode) {
    return seatMapper.findBySeatCode(seatCode);
  }

  @Transactional
  public Seat updateSeatStatus(Long id, SeatStatus status) {
    Seat seat = seatMapper.findById(id)
        .orElseThrow(() -> new ApiException("机位不存在: " + id));
    seatMapper.updateStatus(id, status);
    return seatMapper.findById(id).orElseThrow();
  }

  @Transactional
  public Seat createSeat(Seat seat) {
    seatMapper.insert(seat);
    return seatMapper.findById(seat.getId()).orElseThrow();
  }

  @Transactional
  public Seat updateSeat(Seat seat) {
    seatMapper.findById(seat.getId())
        .orElseThrow(() -> new ApiException("机位不存在: " + seat.getId()));
    seatMapper.update(seat);
    return seatMapper.findById(seat.getId()).orElseThrow();
  }
}
