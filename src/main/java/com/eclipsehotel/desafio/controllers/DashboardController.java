package com.eclipsehotel.desafio.controllers;


import com.eclipsehotel.desafio.models.Customer;
import com.eclipsehotel.desafio.models.Reservation;
import com.eclipsehotel.desafio.models.Room;
import com.eclipsehotel.desafio.services.CustomerService;
import com.eclipsehotel.desafio.services.ReservationService;
import com.eclipsehotel.desafio.services.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final ReservationService reservationService;
    private final RoomService roomService;
    private final CustomerService customerService;

    public DashboardController(ReservationService reservationService, RoomService roomService, CustomerService customerService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.customerService = customerService;
    }

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Estatísticas gerais
        List<Reservation> allReservations = reservationService.getAllReservations();
        List<Room> allRooms = roomService.getAllRooms();
        List<Customer> allCustomers = customerService.getAllCustomers();

        stats.put("totalReservations", allReservations.size());
        stats.put("totalRooms", allRooms.size());
        stats.put("totalCustomers", allCustomers.size());

        // Quartos disponíveis
        long availableRooms = allRooms.stream()
                .filter(room -> "FREE".equals(room.getStatus().toString()) || "DISPONIVEL".equals(room.getStatus().toString()))
                .count();
        stats.put("availableRooms", availableRooms);

        // Reservas ativas (IN_USE)
        long activeReservations = allReservations.stream()
                .filter(reservation -> "IN_USE".equals(reservation.getStatus().toString()))
                .count();
        stats.put("activeReservations", activeReservations);

        // Reservas agendadas (SCHEDULED)
        long scheduledReservations = allReservations.stream()
                .filter(reservation -> "SCHEDULED".equals(reservation.getStatus().toString()))
                .count();
        stats.put("scheduledReservations", scheduledReservations);

        return stats;
    }

    @GetMapping("/recent-activities")
    public List<Reservation> getRecentActivities() {
        // Busca todas as reservas e ordena por data de criação (mais recentes primeiro)
        return reservationService.getAllReservations().stream()
                .sorted((r1, r2) -> r2.getCreateAt().compareTo(r1.getCreateAt()))
                .limit(10) // Últimas 10 atividades
                .toList();
    }

    @GetMapping("/active-reservations")
    public List<Reservation> getActiveReservations() {
        // Reservas que estão atualmente em uso
        return reservationService.getAllReservations().stream()
                .filter(reservation -> "IN_USE".equals(reservation.getStatus().toString()))
                .toList();
    }

    @GetMapping("/upcoming-checkins")
    public List<Reservation> getUpcomingCheckins() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        // Reservas com check-in nas próximas 24 horas
        return reservationService.getAllReservations().stream()
                .filter(reservation -> "SCHEDULED".equals(reservation.getStatus().toString()))
                .filter(reservation -> reservation.getCheckin().isAfter(now) && reservation.getCheckin().isBefore(tomorrow))
                .sorted((r1, r2) -> r1.getCheckin().compareTo(r2.getCheckin()))
                .toList();
    }

    @GetMapping("/available-rooms")
    public List<Room> getAvailableRooms() {
        // Quartos disponíveis
        return roomService.getAllRooms().stream()
                .filter(room -> "FREE".equals(room.getStatus().toString()) || "DISPONIVEL".equals(room.getStatus().toString()))
                .toList();
    }

    @GetMapping("/active-customers")
    public List<Customer> getActiveCustomers() {
        // Clientes que têm reservas ativas ou agendadas
        List<Long> activeCustomerIds = reservationService.getAllReservations().stream()
                .filter(reservation -> "IN_USE".equals(reservation.getStatus().toString()) ||
                        "SCHEDULED".equals(reservation.getStatus().toString()))
                .map(reservation -> reservation.getCustomer().getId())
                .distinct()
                .toList();

        return customerService.getAllCustomers().stream()
                .filter(customer -> activeCustomerIds.contains(customer.getId()))
                .toList();
    }
}



