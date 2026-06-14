package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.PgType;
import java.math.BigDecimal;
import java.util.List;

public interface PgListingService {

	PgListing createPgListing(PgListing pgListing, User owner);

	PgListing updatePgListing(Long id, PgListing updatedPg);

	List<PgListing> getAllPgListings();

	List<PgListing> getApprovedPgListings();

	List<PgListing> getPendingPgListings();

	List<PgListing> getPgListingsByOwner(User owner);

	List<PgListing> searchApprovedPgListings(String keyword);

	List<PgListing> searchApprovedPgListings(String keyword, PgType pgType, BigDecimal minRent, BigDecimal maxRent);

	PgListing approvePgListing(Long id);

	PgListing rejectPgListing(Long id);

	PgListing getPgListingById(Long id);
}
