package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.Complaint;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.ComplaintStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	List<Complaint> findByTenant(User tenant);

	List<Complaint> findByPgListing(PgListing pgListing);

	List<Complaint> findByPgListingIn(List<PgListing> pgListings);

	List<Complaint> findByStatus(ComplaintStatus status);
}
