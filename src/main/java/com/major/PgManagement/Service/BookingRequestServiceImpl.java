package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.BookingRequest;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.Room;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import com.major.pgmanagement.repository.BookingRequestRepository;
import com.major.pgmanagement.repository.PgListingRepository;
import com.major.pgmanagement.repository.RoomRepository;
import com.major.pgmanagement.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingRequestServiceImpl implements BookingRequestService {

	private final BookingRequestRepository bookingRequestRepository;
	private final UserRepository userRepository;
	private final PgListingRepository pgListingRepository;
	private final RoomRepository roomRepository;
	private final TenantAssignmentService tenantAssignmentService;
	private final RentPaymentService rentPaymentService;

	public BookingRequestServiceImpl(
			BookingRequestRepository bookingRequestRepository,
			UserRepository userRepository,
			PgListingRepository pgListingRepository,
			RoomRepository roomRepository,
			TenantAssignmentService tenantAssignmentService,
			RentPaymentService rentPaymentService) {
		this.bookingRequestRepository = bookingRequestRepository;
		this.userRepository = userRepository;
		this.pgListingRepository = pgListingRepository;
		this.roomRepository = roomRepository;
		this.tenantAssignmentService = tenantAssignmentService;
		this.rentPaymentService = rentPaymentService;
	}

	@Override
	@Transactional
	public BookingRequest createBookingRequest(Long tenantId, Long pgListingId, Long roomId, String message) {
		User tenant = userRepository.findById(tenantId)
				.orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));
		PgListing pgListing = getPgListingOrThrow(pgListingId);
		Room room = null;
		if (roomId != null) {
			room = roomRepository.findById(roomId)
					.orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
		}

		BookingRequest bookingRequest = new BookingRequest();
		bookingRequest.setTenant(tenant);
		bookingRequest.setPgListing(pgListing);
		bookingRequest.setRoom(room);
		bookingRequest.setMessage(message);
		bookingRequest.setStatus(BookingStatus.PENDING);
		return bookingRequestRepository.save(bookingRequest);
	}

	@Override
	public List<BookingRequest> getRequestsByTenant(User tenant) {
		return bookingRequestRepository.findByTenant(tenant);
	}

	@Override
	public List<BookingRequest> getRequestsByPgListing(Long pgListingId) {
		return bookingRequestRepository.findByPgListing(getPgListingOrThrow(pgListingId));
	}

	@Override
	public List<BookingRequest> getAllRequests() {
		return bookingRequestRepository.findAll();
	}

	@Override
	public List<BookingRequest> getRequestsByStatus(BookingStatus status) {
		return bookingRequestRepository.findByStatus(status);
	}

	@Override
	public BookingRequest getBookingRequestById(Long requestId) {
		return bookingRequestRepository.findById(requestId)
				.orElseThrow(() -> new RuntimeException("Booking request not found with id: " + requestId));
	}

	@Override
	@Transactional
	public BookingRequest acceptRequest(Long requestId) {
		BookingRequest bookingRequest = getBookingRequestById(requestId);
		if (bookingRequest.getStatus() == BookingStatus.ACCEPTED) {
			throw new RuntimeException("Booking request is already accepted.");
		}

		Room room = bookingRequest.getRoom();
		if (room == null) {
			throw new RuntimeException("Cannot accept booking request without a selected room.");
		}
		if (room.getAvailableBeds() == null || room.getAvailableBeds() <= 0) {
			throw new RuntimeException("No beds are available in room: " + room.getRoomNumber());
		}
		if (tenantAssignmentService.hasActiveAssignment(bookingRequest.getTenant(), bookingRequest.getPgListing())) {
			throw new RuntimeException("Tenant is already assigned to this PG.");
		}

		bookingRequest.setStatus(BookingStatus.ACCEPTED);
		room.setAvailableBeds(room.getAvailableBeds() - 1);
		roomRepository.save(room);
		tenantAssignmentService.createAssignment(bookingRequest.getTenant(), bookingRequest.getPgListing(), room);
		createInitialRentPayment(bookingRequest, room);
		return bookingRequestRepository.save(bookingRequest);
	}

	@Override
	@Transactional
	public BookingRequest rejectRequest(Long requestId) {
		return updateStatus(requestId, BookingStatus.REJECTED);
	}

	@Override
	@Transactional
	public BookingRequest cancelRequest(Long requestId) {
		return updateStatus(requestId, BookingStatus.CANCELLED);
	}

	private BookingRequest updateStatus(Long requestId, BookingStatus status) {
		BookingRequest bookingRequest = getBookingRequestById(requestId);
		bookingRequest.setStatus(status);
		return bookingRequestRepository.save(bookingRequest);
	}

	private PgListing getPgListingOrThrow(Long pgListingId) {
		return pgListingRepository.findById(pgListingId)
				.orElseThrow(() -> new RuntimeException("PG listing not found with id: " + pgListingId));
	}

	private void createInitialRentPayment(BookingRequest bookingRequest, Room room) {
		LocalDate today = LocalDate.now();
		RentPayment rentPayment = new RentPayment();
		rentPayment.setTenant(bookingRequest.getTenant());
		rentPayment.setPgListing(bookingRequest.getPgListing());
		rentPayment.setAmount(room.getRentAmount());
		rentPayment.setMonth(today.getMonth().name());
		rentPayment.setYear(today.getYear());
		rentPaymentService.createRentPayment(rentPayment);
	}
}
