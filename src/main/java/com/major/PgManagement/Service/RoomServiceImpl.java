package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.Room;
import com.major.pgmanagement.repository.PgListingRepository;
import com.major.pgmanagement.repository.RoomRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;
	private final PgListingRepository pgListingRepository;

	public RoomServiceImpl(RoomRepository roomRepository, PgListingRepository pgListingRepository) {
		this.roomRepository = roomRepository;
		this.pgListingRepository = pgListingRepository;
	}

	@Override
	@Transactional
	public Room addRoom(Room room, Long pgListingId) {
		PgListing pgListing = getPgListingOrThrow(pgListingId);
		room.setPgListing(pgListing);
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getRoomsByPgListing(Long pgListingId) {
		return roomRepository.findByPgListing(getPgListingOrThrow(pgListingId));
	}

	@Override
	public List<Room> getAvailableRooms(Long pgListingId) {
		return roomRepository.findByPgListingAndAvailableBedsGreaterThan(getPgListingOrThrow(pgListingId), 0);
	}

	@Override
	public Room getRoomById(Long roomId) {
		return getRoomOrThrow(roomId);
	}

	@Override
	@Transactional
	public Room updateRoom(Long roomId, Room updatedRoom) {
		Room room = getRoomOrThrow(roomId);
		room.setRoomNumber(updatedRoom.getRoomNumber());
		room.setRoomType(updatedRoom.getRoomType());
		room.setRentAmount(updatedRoom.getRentAmount());
		room.setTotalBeds(updatedRoom.getTotalBeds());
		room.setAvailableBeds(updatedRoom.getAvailableBeds());
		room.setDescription(updatedRoom.getDescription());
		return roomRepository.save(room);
	}

	@Override
	@Transactional
	public void deleteRoom(Long roomId) {
		Room room = getRoomOrThrow(roomId);
		roomRepository.delete(room);
	}

	private PgListing getPgListingOrThrow(Long pgListingId) {
		return pgListingRepository.findById(pgListingId)
				.orElseThrow(() -> new RuntimeException("PG listing not found with id: " + pgListingId));
	}

	private Room getRoomOrThrow(Long roomId) {
		return roomRepository.findById(roomId)
				.orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
	}
}
