package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tariff_discount")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TariffDiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;        // "tariff" o "discount"
    private String description; // Ejemplo: "6-10 personas 20%" o "15 vueltas $25000"
    private Float value;         // Si es tarifa = valor, si es descuento = porcentaje
    private String duration;
}
