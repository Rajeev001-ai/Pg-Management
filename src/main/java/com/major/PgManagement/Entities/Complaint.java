package com.major.PgManagement.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String category;

    private String message;

    private String status; // OPEN / RESOLVED

    private String description;

    public Complaint() {}

    public Complaint(Long tenantId, String message, String status) {
        this.tenantId = tenantId;
        this.message = message;
        this.status = status;
    }


}

