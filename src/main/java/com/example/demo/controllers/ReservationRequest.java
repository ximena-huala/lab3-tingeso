package com.example.demo.controllers;

import com.example.demo.entities.ReservationEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationRequest {
    private ReservationEntity reservation;
    private List<String> customerRuts;
}
