package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.Room;
import com.major.pgmanagement.entity.TenantAssignment;
import com.major.pgmanagement.entity.User;
import java.util.List;

public interface TenantAssignmentService {

	TenantAssignment createAssignment(User tenant, PgListing pgListing, Room room);

	boolean hasActiveAssignment(User tenant, PgListing pgListing);

	List<TenantAssignment> getActiveAssignmentsByTenant(User tenant);

	List<TenantAssignment> getActiveAssignmentsByPgListing(PgListing pgListing);
}
