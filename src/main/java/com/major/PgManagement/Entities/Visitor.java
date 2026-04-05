package com.major.PgManagement.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;
    private String visitorName;
    private LocalDateTime inTime;
    private LocalDateTime outTime;

    // getters and setters
}

