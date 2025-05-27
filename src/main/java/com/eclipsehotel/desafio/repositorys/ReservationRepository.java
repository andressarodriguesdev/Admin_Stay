package com.eclipsehotel.desafio.repositorys;

import com.eclipsehotel.desafio.models.Reservation;
import com.eclipsehotel.desafio.models.ReservationStatus;
import com.eclipsehotel.desafio.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Método para buscar reservas entre duas datas
    List<Reservation> findByCheckinBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Método para buscar reservas pelo status
    List<Reservation> findByStatus(ReservationStatus status);

    //Esse método verifica se já existe reserva com status ativo (SCHEDULED, IN_USE...) para o quarto que conflita com o período.
    boolean existsByRoomAndStatusInAndCheckinLessThanEqualAndCheckoutGreaterThanEqual(
            Room room, List<ReservationStatus> statuses, LocalDateTime checkout, LocalDateTime checkin);

}
