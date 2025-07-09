package com.example.demo.services;

import com.example.demo.entities.ReservationEntity;
import com.example.demo.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Map<String, Map<String, Double>> generateTrackTimeReport(int year, int startMonth, int endMonth) {
        Map<String, Map<String, Double>> report = new LinkedHashMap<>();

        List<String> trackOptions = Arrays.asList("10 minutes", "15 minutes", "20 minutes");
        for (String option : trackOptions) {
            report.put(option, new LinkedHashMap<>());
        }

        for (int month = startMonth; month <= endMonth; month++) {
            final int currentMonth = month; // <-- Esto soluciona el problema
            final String monthName = Month.of(currentMonth).name();
            List<ReservationEntity> reservations = reservationRepository.findAll();

            for (String option : trackOptions) {
                double total = reservations.stream()
                        .filter(r -> r.getTrackTime().equals(option)
                                && r.getReservationDate().getMonthValue() == currentMonth)
                        .mapToDouble(ReservationEntity::getTotalPrice)
                        .sum();
                report.get(option).put(monthName, total);
            }
        }


        return report;
    }

    public Map<String, Map<String, Double>> generateGroupSizeReport(int year, int startMonth, int endMonth) {
        Map<String, Map<String, Double>> report = new LinkedHashMap<>();

        String[] groupRanges = {"1-2", "3-5", "6-10", "11-15"};
        for (String range : groupRanges) {
            report.put(range, new LinkedHashMap<>());
        }

        for (int month = startMonth; month <= endMonth; month++) {
            String monthName = Month.of(month).name();
            List<ReservationEntity> reservations = reservationRepository.findAll();

            for (ReservationEntity r : reservations) {
                if (r.getReservationDate().getMonthValue() == month && r.getReservationDate().getYear() == year) {
                    int size = r.getNumberOfPeople();
                    String range = getRangeLabel(size);
                    report.get(range).merge(monthName, r.getTotalPrice(), Double::sum);
                }
            }
        }

        return report;
    }

    private String getRangeLabel(int size) {
        if (size <= 2) return "1-2";
        if (size <= 5) return "3-5";
        if (size <= 10) return "6-10";
        return "11-15";
    }
}
