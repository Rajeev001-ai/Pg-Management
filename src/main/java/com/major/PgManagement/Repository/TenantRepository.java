package com.major.PgManagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.major.PgManagement.Entities.Tenant;



public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByName(String name);

     Optional<Tenant> findByEmail(String email);
}

