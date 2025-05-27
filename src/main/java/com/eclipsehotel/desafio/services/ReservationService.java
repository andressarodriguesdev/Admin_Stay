package com.eclipsehotel.desafio.services;

import com.eclipsehotel.desafio.exceptions.ResourceNotFoundException;
import com.eclipsehotel.desafio.models.*;
import com.eclipsehotel.desafio.repositorys.ReservationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final CustomerService customerService;

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService, CustomerService customerService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.customerService = customerService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com id " + id));
    }

    // Método existente que recebe a reserva completa
    public Reservation createReservation(Reservation reservation) {
        // Validações básicas
        Objects.requireNonNull(reservation.getCheckin(), "Data de check-in é obrigatória.");
        Objects.requireNonNull(reservation.getCheckout(), "Data de check-out é obrigatória.");
        Objects.requireNonNull(reservation.getRoom(), "Quarto da reserva é obrigatório.");
        Objects.requireNonNull(reservation.getRoom().getDailyRate(), "Valor da diária do quarto não pode ser nulo.");

        if (!reservation.getCheckout().isAfter(reservation.getCheckin())) {
            throw new IllegalArgumentException("Data de check-out deve ser depois da data de check-in.");
        }

        // Calcula quantidade de dias (mínimo 1 dia)
        long days = ChronoUnit.DAYS.between(
                reservation.getCheckin().toLocalDate(),
                reservation.getCheckout().toLocalDate()
        );
        if (days == 0) days = 1;


        // Verifica se o quarto está disponível no período
        List<ReservationStatus> activeStatuses = List.of(ReservationStatus.SCHEDULED, ReservationStatus.IN_USE);

        boolean isBooked = reservationRepository.existsByRoomAndStatusInAndCheckinLessThanEqualAndCheckoutGreaterThanEqual(
                reservation.getRoom(),
                activeStatuses,
                reservation.getCheckout(),
                reservation.getCheckin()
        );

        if (isBooked) {
            throw new IllegalStateException("Quarto já está reservado nesse período.");
        }

        // Calcula valor total da reserva
        BigDecimal total = reservation.getRoom().getDailyRate().multiply(BigDecimal.valueOf(days));
        reservation.setTotalValue(total);

        // Seta status da reserva
        reservation.setStatus(ReservationStatus.SCHEDULED);

        // Salva reserva
        Reservation saved = reservationRepository.save(reservation);

        // Atualiza status do quarto para OCCUPIED
        roomService.updateRoomStatus(reservation.getRoom().getId(), RoomStatus.OCCUPIED);

        return saved;
    }

    // Novo método para criar reserva passando customerId e roomNumber
    public Reservation createReservationByIds(Long customerId, String roomNumber, LocalDateTime checkin, LocalDateTime checkout) {
        Customer customer = customerService.getCustomerById(customerId);
        Room room = roomService.getRoomByNumber(roomNumber);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckin(checkin);
        reservation.setCheckout(checkout);

        return createReservation(reservation); // reutiliza o método acima
    }

    public Reservation closeReservation(Long id, ReservationStatus status) {
        if (!List.of(ReservationStatus.FINISHED, ReservationStatus.CANCELED, ReservationStatus.ABSENCE).contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        Reservation reservation = getReservationById(id);
        reservation.setStatus(status);
        Reservation savedReservation = reservationRepository.save(reservation);

        // Liberar quarto após fechar a reserva
        roomService.updateRoomStatus(reservation.getRoom().getId(), RoomStatus.FREE);

        return savedReservation;
    }

    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1);
        return reservationRepository.findByCheckinBetween(startDateTime, endDateTime);
    }

    public List<Reservation> getOccupiedRooms() {
        return reservationRepository.findByStatus(ReservationStatus.IN_USE);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = getReservationById(id); // Vai lançar exceção se não existir
        reservationRepository.delete(reservation);

        // Se quiser, pode atualizar o status do quarto para FREE aqui também
        roomService.updateRoomStatus(reservation.getRoom().getId(), RoomStatus.FREE);
    }

}

