package com.example.demo.repositories;

import com.example.demo.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByReservationDateAndReservationTime(LocalDate date, LocalTime time);
    List<ReservationEntity> findByReservationDate(LocalDate date);
    List<ReservationEntity> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);

}

