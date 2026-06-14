package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.TenantAssignment;
import com.major.pgmanagement.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantAssignmentRepository extends JpaRepository<TenantAssignment, Long> {

	boolean existsByTenantAndPgListingAndActiveTrue(User tenant, PgListing pgListing);

	List<TenantAssignment> findByTenantAndActiveTrue(User tenant);

	List<TenantAssignment> findByPgListingAndActiveTrue(PgListing pgListing);
}
