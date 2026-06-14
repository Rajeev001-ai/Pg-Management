package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByPgListing(PgListing pgListing);

	List<Room> findByPgListingAndAvailableBedsGreaterThan(PgListing pgListing, Integer availableBeds);
}
