package com.example.demo.controllers;

import com.example.demo.entities.ReservationEntity;
import com.example.demo.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.TreeMap;


@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request) {
        try {
            ReservationEntity reservation = reservationService.createReservation(
                    request.getReservation(),
                    request.getCustomerRuts()
            );
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating reservation: " + e.getMessage());
        }
    }

    @GetMapping
    public List<ReservationEntity> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public static class ReservationRequest {
        private ReservationEntity reservation;
        private List<String> customerRuts;

        public ReservationEntity getReservation() {
            return reservation;
        }

        public void setReservation(ReservationEntity reservation) {
            this.reservation = reservation;
        }

        public List<String> getCustomerRuts() {
            return customerRuts;
        }

        public void setCustomerRuts(List<String> customerRuts) {
            this.customerRuts = customerRuts;
        }
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<String>> getAvailableTimes(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<String> availableTimes = reservationService.getAvailableTimesForDate(date);
        return ResponseEntity.ok(availableTimes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservationById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete reservation: " + e.getMessage());
        }
    }

}
