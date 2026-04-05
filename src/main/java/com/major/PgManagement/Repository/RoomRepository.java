package com.major.PgManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.major.PgManagement.Entities.Room;


public interface RoomRepository extends JpaRepository<Room, Integer> {
}


