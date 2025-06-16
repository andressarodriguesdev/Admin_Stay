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


    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getCheckin() {
        return checkin;
    }

    public LocalDateTime getCheckout() {
        return checkout;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public BigDecimal getTotalValue() {
        return totalValue;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCheckin(LocalDateTime checkin) {
        this.checkin = checkin;
    }

    public void setCheckout(LocalDateTime checkout) {
        this.checkout = checkout;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
