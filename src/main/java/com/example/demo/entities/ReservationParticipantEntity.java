package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_participant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonBackReference
    private ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "birthday_discount")
    private Boolean birthdayDiscount = false;

    @Column(name = "individual_discount")
    private Double individualDiscount = 0.0;
}
