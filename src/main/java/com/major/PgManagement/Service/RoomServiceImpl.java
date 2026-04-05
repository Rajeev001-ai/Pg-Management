package com.major.PgManagement.Service;

import org.springframework.stereotype.Service;

import com.major.PgManagement.Entities.Room;
import com.major.PgManagement.Repository.RoomRepository;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;

    public RoomServiceImpl(RoomRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Room> getAllRooms() {
        return repo.findAll();
    }

    @Override
    public Room saveRoom(Room room) {
        return repo.save(room);
    }
}

