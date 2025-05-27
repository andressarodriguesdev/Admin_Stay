package com.eclipsehotel.desafio.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "reservations")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

    public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Room room;

    private LocalDateTime checkin;
    private LocalDateTime checkout;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createAt;

    // Novo campo para valor total da reserva
    private BigDecimal totalValue;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }
}
