package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.Complaint;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.ComplaintStatus;
import com.major.pgmanagement.repository.ComplaintRepository;
import com.major.pgmanagement.repository.PgListingRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintServiceImpl implements ComplaintService {

	private final ComplaintRepository complaintRepository;
	private final PgListingRepository pgListingRepository;

	public ComplaintServiceImpl(ComplaintRepository complaintRepository, PgListingRepository pgListingRepository) {
		this.complaintRepository = complaintRepository;
		this.pgListingRepository = pgListingRepository;
	}

	@Override
	@Transactional
	public Complaint createComplaint(Complaint complaint, User tenant, Long pgListingId) {
		PgListing pgListing = getPgListingOrThrow(pgListingId);
		complaint.setTenant(tenant);
		complaint.setPgListing(pgListing);
		complaint.setStatus(ComplaintStatus.OPEN);
		return complaintRepository.save(complaint);
	}

	@Override
	public List<Complaint> getComplaintsByTenant(User tenant) {
		return complaintRepository.findByTenant(tenant);
	}

	@Override
	public List<Complaint> getComplaintsByPgListing(Long pgListingId) {
		return complaintRepository.findByPgListing(getPgListingOrThrow(pgListingId));
	}

	@Override
	public List<Complaint> getAllComplaints() {
		return complaintRepository.findAll();
	}

	@Override
	public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
		return complaintRepository.findByStatus(status);
	}

	@Override
	public List<Complaint> getComplaintsByOwner(User owner) {
		List<PgListing> pgListings = pgListingRepository.findByOwner(owner);
		if (pgListings.isEmpty()) {
			return List.of();
		}
		return complaintRepository.findByPgListingIn(pgListings);
	}

	@Override
	public Complaint getComplaintById(Long complaintId) {
		return getComplaintOrThrow(complaintId);
	}

	@Override
	@Transactional
	public Complaint markInProgress(Long complaintId) {
		return updateStatus(complaintId, ComplaintStatus.IN_PROGRESS);
	}

	@Override
	@Transactional
	public Complaint markResolved(Long complaintId) {
		return updateStatus(complaintId, ComplaintStatus.RESOLVED);
	}

	private Complaint updateStatus(Long complaintId, ComplaintStatus status) {
		Complaint complaint = getComplaintOrThrow(complaintId);
		complaint.setStatus(status);
		return complaintRepository.save(complaint);
	}

	private Complaint getComplaintOrThrow(Long complaintId) {
		return complaintRepository.findById(complaintId)
				.orElseThrow(() -> new RuntimeException("Complaint not found with id: " + complaintId));
	}

	private PgListing getPgListingOrThrow(Long pgListingId) {
		return pgListingRepository.findById(pgListingId)
				.orElseThrow(() -> new RuntimeException("PG listing not found with id: " + pgListingId));
	}
}
