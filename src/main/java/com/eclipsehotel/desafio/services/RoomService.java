package com.eclipsehotel.desafio.services;

import com.eclipsehotel.desafio.exceptions.ResourceNotFoundException;
import com.eclipsehotel.desafio.models.Room;
import com.eclipsehotel.desafio.models.RoomStatus;
import com.eclipsehotel.desafio.repositorys.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quarto não encontrado com id " + id));
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) {
        Room existingRoom = getRoomById(id);
        existingRoom.setNumber(room.getNumber());
        existingRoom.setDailyRate(room.getDailyRate());
        existingRoom.setStatus(room.getStatus());
        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {
        Room existingRoom = getRoomById(id);
        roomRepository.delete(existingRoom);
    }

    public List<Room> getOccupiedRooms() {
        return roomRepository.findByStatus(RoomStatus.OCCUPIED);
    }

    public Room updateRoomStatus(Long id, RoomStatus status) {
        Room room = getRoomById(id);
        room.setStatus(status);
        return roomRepository.save(room);
    }

    public Room getRoomByNumber(String number) {
        return roomRepository.findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Quarto não encontrado com número " + number));
    }



}
