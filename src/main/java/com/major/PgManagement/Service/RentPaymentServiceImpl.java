package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.RentStatus;
import com.major.pgmanagement.repository.PgListingRepository;
import com.major.pgmanagement.repository.RentPaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentPaymentServiceImpl implements RentPaymentService {

	private final RentPaymentRepository rentPaymentRepository;
	private final PgListingRepository pgListingRepository;

	public RentPaymentServiceImpl(RentPaymentRepository rentPaymentRepository, PgListingRepository pgListingRepository) {
		this.rentPaymentRepository = rentPaymentRepository;
		this.pgListingRepository = pgListingRepository;
	}

	@Override
	@Transactional
	public RentPayment createRentPayment(RentPayment rentPayment) {
		rentPayment.setStatus(RentStatus.PENDING);
		return rentPaymentRepository.save(rentPayment);
	}

	@Override
	public List<RentPayment> getRentPaymentsByTenant(User tenant) {
		return rentPaymentRepository.findByTenant(tenant);
	}

	@Override
	public List<RentPayment> getRentPaymentsByPgListing(Long pgListingId) {
		PgListing pgListing = pgListingRepository.findById(pgListingId)
				.orElseThrow(() -> new RuntimeException("PG listing not found with id: " + pgListingId));
		return rentPaymentRepository.findByPgListing(pgListing);
	}

	@Override
	public List<RentPayment> getAllRentPayments() {
		return rentPaymentRepository.findAll();
	}

	@Override
	public List<RentPayment> getRentPaymentsByStatus(RentStatus status) {
		return rentPaymentRepository.findByStatus(status);
	}

	@Override
	public List<RentPayment> getRentPaymentsByOwner(User owner) {
		List<PgListing> pgListings = pgListingRepository.findByOwner(owner);
		if (pgListings.isEmpty()) {
			return List.of();
		}
		return rentPaymentRepository.findByPgListingIn(pgListings);
	}

	@Override
	public RentPayment getRentPaymentById(Long paymentId) {
		return getRentPaymentOrThrow(paymentId);
	}

	@Override
	@Transactional
	public RentPayment markAsPaid(Long paymentId) {
		RentPayment rentPayment = getRentPaymentOrThrow(paymentId);
		rentPayment.setStatus(RentStatus.PAID);
		rentPayment.setPaymentDate(LocalDateTime.now());
		return rentPaymentRepository.save(rentPayment);
	}

	@Override
	@Transactional
	public RentPayment markAsOverdue(Long paymentId) {
		RentPayment rentPayment = getRentPaymentOrThrow(paymentId);
		rentPayment.setStatus(RentStatus.OVERDUE);
		return rentPaymentRepository.save(rentPayment);
	}

	private RentPayment getRentPaymentOrThrow(Long paymentId) {
		return rentPaymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Rent payment not found with id: " + paymentId));
	}
}
