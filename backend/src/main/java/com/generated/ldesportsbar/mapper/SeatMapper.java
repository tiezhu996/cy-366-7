package com.generated.ldesportsbar.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.generated.ldesportsbar.enums.SeatStatus;
import com.generated.ldesportsbar.model.Seat;

@Mapper
public interface SeatMapper {
  List<Seat> findAll();
  List<Seat> findByStatus(@Param("status") SeatStatus status);
  List<Seat> findByArea(@Param("areaName") String areaName);
  Optional<Seat> findById(@Param("id") Long id);
  Optional<Seat> findBySeatCode(@Param("seatCode") String seatCode);
  int updateStatus(@Param("id") Long id, @Param("status") SeatStatus status);
  int insert(Seat seat);
  int update(Seat seat);
}
