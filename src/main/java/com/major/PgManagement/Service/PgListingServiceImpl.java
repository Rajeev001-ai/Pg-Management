package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import com.major.pgmanagement.repository.PgListingRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PgListingServiceImpl implements PgListingService {

	private final PgListingRepository pgListingRepository;

	public PgListingServiceImpl(PgListingRepository pgListingRepository) {
		this.pgListingRepository = pgListingRepository;
	}

	@Override
	@Transactional
	public PgListing createPgListing(PgListing pgListing, User owner) {
		pgListing.setOwner(owner);
		pgListing.setStatus(PgStatus.PENDING);
		return pgListingRepository.save(pgListing);
	}

	@Override
	@Transactional
	public PgListing updatePgListing(Long id, PgListing updatedPg) {
		PgListing pgListing = getPgListingById(id);
		pgListing.setPgName(updatedPg.getPgName());
		pgListing.setDescription(updatedPg.getDescription());
		pgListing.setAddress(updatedPg.getAddress());
		pgListing.setCity(updatedPg.getCity());
		pgListing.setArea(updatedPg.getArea());
		pgListing.setLatitude(updatedPg.getLatitude());
		pgListing.setLongitude(updatedPg.getLongitude());
		pgListing.setPgType(updatedPg.getPgType());
		pgListing.setMonthlyRentStartingFrom(updatedPg.getMonthlyRentStartingFrom());
		pgListing.setFacilities(updatedPg.getFacilities());
		pgListing.setRules(updatedPg.getRules());
		pgListing.setImageUrl(updatedPg.getImageUrl());
		return pgListingRepository.save(pgListing);
	}

	@Override
	public List<PgListing> getAllPgListings() {
		return pgListingRepository.findAll();
	}

	@Override
	public List<PgListing> getApprovedPgListings() {
		return pgListingRepository.findByStatus(PgStatus.APPROVED);
	}

	@Override
	public List<PgListing> getPendingPgListings() {
		return pgListingRepository.findByStatus(PgStatus.PENDING);
	}

	@Override
	public List<PgListing> getPgListingsByOwner(User owner) {
		return pgListingRepository.findByOwner(owner);
	}

	@Override
	public List<PgListing> searchApprovedPgListings(String keyword) {
		return pgListingRepository.findByStatusAndCityContainingIgnoreCaseOrStatusAndAreaContainingIgnoreCase(
				PgStatus.APPROVED,
				keyword,
				PgStatus.APPROVED,
				keyword);
	}

	@Override
	public List<PgListing> searchApprovedPgListings(String keyword, PgType pgType, BigDecimal minRent, BigDecimal maxRent) {
		String cleanKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
		return pgListingRepository.searchApprovedPgListings(PgStatus.APPROVED, cleanKeyword, pgType, minRent, maxRent);
	}

	@Override
	@Transactional
	public PgListing approvePgListing(Long id) {
		PgListing pgListing = getPgListingById(id);
		pgListing.setStatus(PgStatus.APPROVED);
		return pgListingRepository.save(pgListing);
	}

	@Override
	@Transactional
	public PgListing rejectPgListing(Long id) {
		PgListing pgListing = getPgListingById(id);
		pgListing.setStatus(PgStatus.REJECTED);
		return pgListingRepository.save(pgListing);
	}

	@Override
	public PgListing getPgListingById(Long id) {
		return pgListingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("PG listing not found with id: " + id));
	}
}
