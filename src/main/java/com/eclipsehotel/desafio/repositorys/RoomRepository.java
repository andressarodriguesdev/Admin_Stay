package com.eclipsehotel.desafio.repositorys;

import com.eclipsehotel.desafio.models.Room;
import com.eclipsehotel.desafio.models.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.id IN (SELECT res.room.id FROM Reservation res WHERE res.status = 'INDISPONIVEL')")
    List<Room> findOccupiedRooms();
    List<Room> findByStatus(RoomStatus status);
    Optional<Room> findByNumber(String number);
}
