package com.major.PgManagement.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Room {
    @Id
    private int roomNo;
    private String type; // Single/Double/Triple
    private double rent;
    private String status; // Vacant/Occupied

    // getters and setters
}


