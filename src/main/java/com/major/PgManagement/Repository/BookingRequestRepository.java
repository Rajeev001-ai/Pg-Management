package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.BookingRequest;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

	List<BookingRequest> findByTenant(User tenant);

	List<BookingRequest> findByPgListing(PgListing pgListing);

	List<BookingRequest> findByStatus(BookingStatus status);
}
