package com.example.demo.controllers;

import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*") // Esto es temporal para pruebas. Se puede limitar más adelante.
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/by-track-time")
    public Map<String, Map<String, Double>> getReportByTrackTime(
            @RequestParam int year,
            @RequestParam int startMonth,
            @RequestParam int endMonth) {
        return reportService.generateTrackTimeReport(year, startMonth, endMonth);
    }

    @GetMapping("/by-group-size")
    public Map<String, Map<String, Double>> getReportByGroupSize(
            @RequestParam int year,
            @RequestParam int startMonth,
            @RequestParam int endMonth) {
        return reportService.generateGroupSizeReport(year, startMonth, endMonth);
    }
}

