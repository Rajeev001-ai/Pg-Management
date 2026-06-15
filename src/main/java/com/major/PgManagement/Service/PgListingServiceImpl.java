package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import com.major.pgmanagement.repository.PgListingRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
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
		return pgListingRepository.findAll((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(root.get("status"), PgStatus.APPROVED));

			if (StringUtils.hasText(cleanKeyword)) {
				String searchPattern = "%" + cleanKeyword.toLowerCase() + "%";
				Predicate cityMatches = criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), searchPattern);
				Predicate areaMatches = criteriaBuilder.like(criteriaBuilder.lower(root.get("area")), searchPattern);
				predicates.add(criteriaBuilder.or(cityMatches, areaMatches));
			}

			if (pgType != null) {
				predicates.add(criteriaBuilder.equal(root.get("pgType"), pgType));
			}

			if (minRent != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("monthlyRentStartingFrom"), minRent));
			}

			if (maxRent != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("monthlyRentStartingFrom"), maxRent));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
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
