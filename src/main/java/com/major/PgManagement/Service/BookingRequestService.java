package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.BookingRequest;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import java.util.List;

public interface BookingRequestService {

	BookingRequest createBookingRequest(Long tenantId, Long pgListingId, Long roomId, String message);

	List<BookingRequest> getRequestsByTenant(User tenant);

	List<BookingRequest> getRequestsByPgListing(Long pgListingId);

	List<BookingRequest> getAllRequests();

	List<BookingRequest> getRequestsByStatus(BookingStatus status);

	BookingRequest getBookingRequestById(Long requestId);

	BookingRequest acceptRequest(Long requestId);

	BookingRequest rejectRequest(Long requestId);

	BookingRequest cancelRequest(Long requestId);
}
