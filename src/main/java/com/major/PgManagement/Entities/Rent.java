package com.major.PgManagement.Entities;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private double amount;

    private String month;

    private String status; // PAID / PENDING

    private LocalDateTime paymentDate;

    public Rent() {}

    public Rent(Long tenantId, double amount, String month, String status) {
        this.tenantId = tenantId;
        this.amount = amount;
        this.month = month;
        this.status = status;
    }

}

