package com.eclipsehotel.desafio.controllers;

import com.eclipsehotel.desafio.exceptions.ResourceNotFoundException;
import com.eclipsehotel.desafio.models.Reservation;
import com.eclipsehotel.desafio.models.ReservationStatus;
import com.eclipsehotel.desafio.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Classe auxiliar para o corpo da requisição simplificada
    public static class ReservationRequest {
        public Long customerId;
        public String roomNumber;
        public String checkin;
        public String checkout;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime checkin = LocalDateTime.parse(request.checkin, formatter);
            LocalDateTime checkout = LocalDateTime.parse(request.checkout, formatter);

            Reservation reservation = reservationService.createReservationByIds(
                    request.customerId,
                    request.roomNumber,
                    checkin,
                    checkout
            );
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public Reservation closeReservation(@PathVariable Long id, @RequestParam ReservationStatus status) {
        return reservationService.closeReservation(id, status);
    }

    @GetMapping("/date-range")
    public List<Reservation> getReservationsByDateRange(
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate.trim(), formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate.trim(), formatter);

        return reservationService.getReservationsByDateRange(startLocalDate, endLocalDate);
    }

    @GetMapping("/occupied-rooms")
    public List<Reservation> getOccupiedRooms() {
        return reservationService.getOccupiedRooms();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

}
