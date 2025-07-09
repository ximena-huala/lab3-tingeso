package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Entity
@Table(name = "karts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class KartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    @Column(unique = true, nullable = false)
    private String code;

    private String status; // Ej: disponible, en mantenimiento, fuera de servicio
}
