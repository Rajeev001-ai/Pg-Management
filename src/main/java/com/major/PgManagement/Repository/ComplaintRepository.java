package com.major.PgManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.major.PgManagement.Entities.Complaint;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByTenantId(Long tenantId);
}



