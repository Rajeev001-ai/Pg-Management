package com.major.PgManagement.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user") // avoid reserved keyword "user"
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // can be email or phone

    private String phone;

    @Column(nullable = false)
    private String password; // stored encoded (BCrypt)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // optional link to Tenant record (for ROLE_TENANT)
    private Long tenantId;

}

