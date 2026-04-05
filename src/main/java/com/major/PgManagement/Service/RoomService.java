package com.major.PgManagement.Service;

import java.util.List;

import com.major.PgManagement.Entities.Room;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    Room saveRoom(Room room);
}

