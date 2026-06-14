package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.Complaint;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.ComplaintStatus;
import java.util.List;

public interface ComplaintService {

	Complaint createComplaint(Complaint complaint, User tenant, Long pgListingId);

	List<Complaint> getComplaintsByTenant(User tenant);

	List<Complaint> getComplaintsByPgListing(Long pgListingId);

	List<Complaint> getAllComplaints();

	List<Complaint> getComplaintsByStatus(ComplaintStatus status);

	List<Complaint> getComplaintsByOwner(User owner);

	Complaint getComplaintById(Long complaintId);

	Complaint markInProgress(Long complaintId);

	Complaint markResolved(Long complaintId);
}
