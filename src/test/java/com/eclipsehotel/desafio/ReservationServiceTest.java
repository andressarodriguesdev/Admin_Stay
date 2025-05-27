package com.eclipsehotel.desafio;

import com.eclipsehotel.desafio.models.*;
import com.eclipsehotel.desafio.repositorys.ReservationRepository;
import com.eclipsehotel.desafio.services.CustomerService;
import com.eclipsehotel.desafio.services.ReservationService;
import com.eclipsehotel.desafio.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private RoomService roomService;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        roomService = mock(RoomService.class);
        customerService = mock(CustomerService.class);
        reservationService = new ReservationService(reservationRepository, roomService, customerService);
    }

    @Test // testar o cálculo das diárias
    void testCalculateTotalValueCorrectlyForTwoNights() {
        // Dados simulados
        Customer customer = new Customer();
        customer.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setNumber("101");
        room.setDailyRate(new BigDecimal("150"));

        LocalDateTime checkin = LocalDateTime.of(2025, 5, 28, 14, 0);
        LocalDateTime checkout = LocalDateTime.of(2025, 5, 30, 12, 0); // 2 diárias

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(roomService.getRoomByNumber("101")).thenReturn(room);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        // Quando
        Reservation reservation = reservationService.createReservationByIds(1L, "101", checkin, checkout);

        // Então
        assertNotNull(reservation);
        assertEquals(new BigDecimal("300"), reservation.getTotalValue()); // 150 * 2
        assertEquals(ReservationStatus.SCHEDULED, reservation.getStatus());
    }

    @Test // Esperamos que ele dispare uma exceção porque a data é inválida.
    void testCreateReservationThrowsWhenCheckoutBeforeCheckin() {
        Customer customer = new Customer();
        customer.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setNumber("101");
        room.setDailyRate(new BigDecimal("150"));

        LocalDateTime checkin = LocalDateTime.of(2025, 5, 30, 14, 0);
        LocalDateTime checkout = LocalDateTime.of(2025, 5, 28, 12, 0); // inválido

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(roomService.getRoomByNumber("101")).thenReturn(room);

        assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservationByIds(1L, "101", checkin, checkout)
        );
    }

    @Test //Esperamos exceção de estado, porque o quarto já está ocupado no período.
    void testCreateReservationThrowsWhenRoomAlreadyBooked() {
        Customer customer = new Customer();
        customer.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setNumber("101");
        room.setDailyRate(new BigDecimal("150"));

        LocalDateTime checkin = LocalDateTime.of(2025, 5, 28, 14, 0);
        LocalDateTime checkout = LocalDateTime.of(2025, 5, 30, 12, 0);

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(roomService.getRoomByNumber("101")).thenReturn(room);
        when(reservationRepository.existsByRoomAndStatusInAndCheckinLessThanEqualAndCheckoutGreaterThanEqual(
                eq(room), anyList(), eq(checkout), eq(checkin)))
                .thenReturn(true); // quarto já está reservado

        assertThrows(IllegalStateException.class, () ->
                reservationService.createReservationByIds(1L, "101", checkin, checkout)
        );
    }

    @Test //Esse teste garante que, ao finalizar a reserva, o sistema:

    //Atualiza o status da reserva;

    //Libera o quarto.
    void testCloseReservationSetsStatusAndFreesRoom() {
        Room room = new Room();
        room.setId(1L);
        room.setStatus(RoomStatus.OCCUPIED);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setRoom(room);
        reservation.setStatus(ReservationStatus.IN_USE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation closed = reservationService.closeReservation(1L, ReservationStatus.FINISHED);

        assertEquals(ReservationStatus.FINISHED, closed.getStatus());
        verify(roomService).updateRoomStatus(1L, RoomStatus.FREE);
    }


}
