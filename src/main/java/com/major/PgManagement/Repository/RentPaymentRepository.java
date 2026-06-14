package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.RentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentPaymentRepository extends JpaRepository<RentPayment, Long> {

	List<RentPayment> findByTenant(User tenant);

	List<RentPayment> findByPgListing(PgListing pgListing);

	List<RentPayment> findByPgListingIn(List<PgListing> pgListings);

	List<RentPayment> findByStatus(RentStatus status);
}
