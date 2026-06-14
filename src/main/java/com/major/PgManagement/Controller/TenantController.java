package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.BookingRequest;
import com.major.pgmanagement.entity.Complaint;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import com.major.pgmanagement.service.BookingRequestService;
import com.major.pgmanagement.service.ComplaintService;
import com.major.pgmanagement.service.PgListingService;
import com.major.pgmanagement.service.RentPaymentService;
import com.major.pgmanagement.service.RoomService;
import com.major.pgmanagement.service.TenantAssignmentService;
import com.major.pgmanagement.service.UserService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TenantController {

	private final UserService userService;
	private final BookingRequestService bookingRequestService;
	private final ComplaintService complaintService;
	private final RentPaymentService rentPaymentService;
	private final PgListingService pgListingService;
	private final RoomService roomService;
	private final TenantAssignmentService tenantAssignmentService;

	public TenantController(
			UserService userService,
			BookingRequestService bookingRequestService,
			ComplaintService complaintService,
			RentPaymentService rentPaymentService,
			PgListingService pgListingService,
			RoomService roomService,
			TenantAssignmentService tenantAssignmentService) {
		this.userService = userService;
		this.bookingRequestService = bookingRequestService;
		this.complaintService = complaintService;
		this.rentPaymentService = rentPaymentService;
		this.pgListingService = pgListingService;
		this.roomService = roomService;
		this.tenantAssignmentService = tenantAssignmentService;
	}

	@GetMapping("/tenant/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		User tenant = getLoggedInTenant(authentication);
		List<BookingRequest> bookingRequests = bookingRequestService.getRequestsByTenant(tenant);
		List<Complaint> complaints = complaintService.getComplaintsByTenant(tenant);
		List<RentPayment> rentPayments = rentPaymentService.getRentPaymentsByTenant(tenant);

		long acceptedBookings = bookingRequests.stream()
				.filter(request -> request.getStatus() == BookingStatus.ACCEPTED)
				.count();
		long pendingBookings = bookingRequests.stream()
				.filter(request -> request.getStatus() == BookingStatus.PENDING)
				.count();

		model.addAttribute("bookingRequestsCount", bookingRequests.size());
		model.addAttribute("acceptedBookingsCount", acceptedBookings);
		model.addAttribute("pendingBookingsCount", pendingBookings);
		model.addAttribute("complaintsCount", complaints.size());
		model.addAttribute("rentPaymentsCount", rentPayments.size());
		return "tenant/dashboard";
	}

	@GetMapping("/tenant/search-pg")
	public String searchPg(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) PgType pgType,
			@RequestParam(required = false) BigDecimal minRent,
			@RequestParam(required = false) BigDecimal maxRent,
			Model model) {
		model.addAttribute("pgListings", pgListingService.searchApprovedPgListings(keyword, pgType, minRent, maxRent));
		addSearchAttributes(model, keyword, pgType, minRent, maxRent);
		return "tenant/search-pg";
	}

	@GetMapping("/tenant/search-pg/details/{id}")
	public String pgDetail(@PathVariable Long id, Model model) {
		PgListing pgListing = pgListingService.getPgListingById(id);
		if (pgListing.getStatus() != PgStatus.APPROVED) {
			throw new RuntimeException("Approved PG listing not found with id: " + id);
		}
		model.addAttribute("pg", pgListing);
		model.addAttribute("availableRooms", roomService.getAvailableRooms(id));
		return "tenant/pg-detail";
	}

	@GetMapping("/tenant/bookings")
	public String bookings(Authentication authentication, Model model) {
		User tenant = getLoggedInTenant(authentication);
		model.addAttribute("bookingRequests", bookingRequestService.getRequestsByTenant(tenant));
		return "tenant/booking-list";
	}

	@GetMapping("/tenant/bookings/create/{pgId}")
	public String bookingForm(@PathVariable Long pgId, Model model) {
		PgListing pgListing = pgListingService.getPgListingById(pgId);
		model.addAttribute("pgListing", pgListing);
		model.addAttribute("availableRooms", roomService.getAvailableRooms(pgId));
		model.addAttribute("bookingRequest", new BookingRequest());
		return "tenant/booking-form";
	}

	@PostMapping("/tenant/bookings/create/{pgId}")
	public String createBooking(
			@PathVariable Long pgId,
			@Valid @ModelAttribute BookingRequest bookingRequest,
			BindingResult bindingResult,
			@RequestParam(required = false) Long roomId,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {
		User tenant = getLoggedInTenant(authentication);
		if (bindingResult.hasErrors()) {
			PgListing pgListing = pgListingService.getPgListingById(pgId);
			model.addAttribute("pgListing", pgListing);
			model.addAttribute("availableRooms", roomService.getAvailableRooms(pgId));
			return "tenant/booking-form";
		}
		bookingRequestService.createBookingRequest(tenant.getId(), pgId, roomId, bookingRequest.getMessage());
		redirectAttributes.addFlashAttribute("success", "Booking request sent successfully.");
		return "redirect:/tenant/bookings";
	}

	@PostMapping("/tenant/bookings/cancel/{id}")
	public String cancelBooking(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User tenant = getLoggedInTenant(authentication);
		BookingRequest bookingRequest = getTenantBookingRequest(id, tenant);
		if (bookingRequest.getStatus() != BookingStatus.PENDING) {
			throw new RuntimeException("Only pending booking requests can be cancelled.");
		}
		bookingRequestService.cancelRequest(id);
		redirectAttributes.addFlashAttribute("success", "Booking request cancelled.");
		return "redirect:/tenant/bookings";
	}

	@GetMapping("/tenant/complaints")
	public String complaints(Authentication authentication, Model model) {
		User tenant = getLoggedInTenant(authentication);
		model.addAttribute("complaints", complaintService.getComplaintsByTenant(tenant));
		return "tenant/complaint-list";
	}

	@GetMapping("/tenant/complaints/create/{pgId}")
	public String complaintForm(@PathVariable Long pgId, Authentication authentication, Model model) {
		User tenant = getLoggedInTenant(authentication);
		PgListing pgListing = pgListingService.getPgListingById(pgId);
		if (!tenantAssignmentService.hasActiveAssignment(tenant, pgListing)) {
			throw new RuntimeException("You can raise a complaint only after an accepted booking and active assignment.");
		}
		model.addAttribute("pgListing", pgListing);
		model.addAttribute("complaint", new Complaint());
		return "tenant/complaint-form";
	}

	@PostMapping("/tenant/complaints/create/{pgId}")
	public String createComplaint(
			@PathVariable Long pgId,
			@Valid @ModelAttribute Complaint complaint,
			BindingResult bindingResult,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {
		User tenant = getLoggedInTenant(authentication);
		PgListing pgListing = pgListingService.getPgListingById(pgId);
		if (!tenantAssignmentService.hasActiveAssignment(tenant, pgListing)) {
			throw new RuntimeException("You can raise a complaint only after an accepted booking and active assignment.");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("pgListing", pgListing);
			return "tenant/complaint-form";
		}
		complaintService.createComplaint(complaint, tenant, pgId);
		redirectAttributes.addFlashAttribute("success", "Complaint submitted successfully.");
		return "redirect:/tenant/complaints";
	}

	@GetMapping("/tenant/rent-payments")
	public String rentPayments(Authentication authentication, Model model) {
		User tenant = getLoggedInTenant(authentication);
		model.addAttribute("rentPayments", rentPaymentService.getRentPaymentsByTenant(tenant));
		return "tenant/rent-payments";
	}

	@GetMapping("/tenant/profile")
	public String profile(Authentication authentication, Model model) {
		User tenant = getLoggedInTenant(authentication);
		model.addAttribute("user", tenant);
		return "tenant/profile";
	}

	private User getLoggedInTenant(Authentication authentication) {
		return userService.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Logged-in tenant not found."));
	}

	private BookingRequest getTenantBookingRequest(Long requestId, User tenant) {
		BookingRequest bookingRequest = bookingRequestService.getBookingRequestById(requestId);
		if (bookingRequest.getTenant() == null || !bookingRequest.getTenant().getId().equals(tenant.getId())) {
			throw new RuntimeException("You are not allowed to manage this booking request.");
		}
		return bookingRequest;
	}

	private void addSearchAttributes(Model model, String keyword, PgType pgType, BigDecimal minRent, BigDecimal maxRent) {
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedPgType", pgType);
		model.addAttribute("minRent", minRent);
		model.addAttribute("maxRent", maxRent);
		model.addAttribute("pgTypes", PgType.values());
	}
}
