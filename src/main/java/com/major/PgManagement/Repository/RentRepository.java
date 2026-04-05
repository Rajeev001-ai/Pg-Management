package com.major.PgManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.major.PgManagement.Entities.Rent;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByTenantId(Long tenantId);
}


