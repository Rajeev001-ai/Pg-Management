package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.Room;
import java.util.List;

public interface RoomService {

	Room addRoom(Room room, Long pgListingId);

	List<Room> getRoomsByPgListing(Long pgListingId);

	List<Room> getAvailableRooms(Long pgListingId);

	Room getRoomById(Long roomId);

	Room updateRoom(Long roomId, Room updatedRoom);

	void deleteRoom(Long roomId);
}
