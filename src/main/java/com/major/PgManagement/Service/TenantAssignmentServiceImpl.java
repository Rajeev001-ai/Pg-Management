package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.Room;
import com.major.pgmanagement.entity.TenantAssignment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.repository.TenantAssignmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantAssignmentServiceImpl implements TenantAssignmentService {

	private final TenantAssignmentRepository tenantAssignmentRepository;

	public TenantAssignmentServiceImpl(TenantAssignmentRepository tenantAssignmentRepository) {
		this.tenantAssignmentRepository = tenantAssignmentRepository;
	}

	@Override
	@Transactional
	public TenantAssignment createAssignment(User tenant, PgListing pgListing, Room room) {
		if (hasActiveAssignment(tenant, pgListing)) {
			throw new RuntimeException("Tenant is already assigned to this PG.");
		}

		TenantAssignment assignment = new TenantAssignment();
		assignment.setTenant(tenant);
		assignment.setPgListing(pgListing);
		assignment.setRoom(room);
		assignment.setStartDate(LocalDateTime.now());
		assignment.setActive(true);
		return tenantAssignmentRepository.save(assignment);
	}

	@Override
	public boolean hasActiveAssignment(User tenant, PgListing pgListing) {
		return tenantAssignmentRepository.existsByTenantAndPgListingAndActiveTrue(tenant, pgListing);
	}

	@Override
	public List<TenantAssignment> getActiveAssignmentsByTenant(User tenant) {
		return tenantAssignmentRepository.findByTenantAndActiveTrue(tenant);
	}

	@Override
	public List<TenantAssignment> getActiveAssignmentsByPgListing(PgListing pgListing) {
		return tenantAssignmentRepository.findByPgListingAndActiveTrue(pgListing);
	}
}
