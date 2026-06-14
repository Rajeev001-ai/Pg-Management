package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import com.major.pgmanagement.entity.enums.ComplaintStatus;
import com.major.pgmanagement.entity.enums.RentStatus;
import com.major.pgmanagement.service.BookingRequestService;
import com.major.pgmanagement.service.ComplaintService;
import com.major.pgmanagement.service.PgListingService;
import com.major.pgmanagement.service.RentPaymentService;
import com.major.pgmanagement.service.UserService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

	private final UserService userService;
	private final PgListingService pgListingService;
	private final BookingRequestService bookingRequestService;
	private final ComplaintService complaintService;
	private final RentPaymentService rentPaymentService;

	public AdminController(
			UserService userService,
			PgListingService pgListingService,
			BookingRequestService bookingRequestService,
			ComplaintService complaintService,
			RentPaymentService rentPaymentService) {
		this.userService = userService;
		this.pgListingService = pgListingService;
		this.bookingRequestService = bookingRequestService;
		this.complaintService = complaintService;
		this.rentPaymentService = rentPaymentService;
	}

	@GetMapping("/admin/dashboard")
	public String dashboard(Model model) {
		List<User> users = userService.getAllUsers();
		List<User> owners = userService.getAllOwners();
		List<User> tenants = userService.getAllTenants();
		List<User> pendingOwners = userService.getPendingOwners();
		List<PgListing> pgListings = pgListingService.getAllPgListings();
		List<PgListing> pendingPgListings = pgListingService.getPendingPgListings();
		List<PgListing> approvedPgListings = pgListingService.getApprovedPgListings();

		model.addAttribute("totalUsers", users.size());
		model.addAttribute("totalOwners", owners.size());
		model.addAttribute("totalTenants", tenants.size());
		model.addAttribute("pendingOwners", pendingOwners.size());
		model.addAttribute("totalPgListings", pgListings.size());
		model.addAttribute("pendingPgListings", pendingPgListings.size());
		model.addAttribute("approvedPgListings", approvedPgListings.size());
		model.addAttribute("totalBookingRequests", bookingRequestService.getAllRequests().size());
		model.addAttribute("pendingBookingRequests", bookingRequestService.getRequestsByStatus(BookingStatus.PENDING).size());
		model.addAttribute("totalComplaints", complaintService.getAllComplaints().size());
		model.addAttribute("openComplaints", complaintService.getComplaintsByStatus(ComplaintStatus.OPEN).size());
		model.addAttribute("totalRentPayments", rentPaymentService.getAllRentPayments().size());
		model.addAttribute("pendingRentPayments", rentPaymentService.getRentPaymentsByStatus(RentStatus.PENDING).size());
		return "admin/dashboard";
	}

	@GetMapping("/admin/owners")
	public String owners(Model model) {
		model.addAttribute("owners", userService.getAllOwners());
		return "admin/owners";
	}

	@GetMapping("/admin/owners/pending")
	public String pendingOwners(Model model) {
		model.addAttribute("owners", userService.getPendingOwners());
		return "admin/pending-owners";
	}

	@GetMapping("/admin/users")
	public String users(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "admin/users";
	}

	@GetMapping("/admin/tenants")
	public String tenants(Model model) {
		model.addAttribute("tenants", userService.getAllTenants());
		return "admin/tenants";
	}

	@GetMapping("/admin/pg-listings")
	public String pgListings(Model model) {
		model.addAttribute("pgListings", pgListingService.getAllPgListings());
		model.addAttribute("title", "All PG Listings");
		model.addAttribute("pendingOnly", false);
		return "admin/pg-listings";
	}

	@GetMapping("/admin/pg-listings/pending")
	public String pendingPgListings(Model model) {
		model.addAttribute("pgListings", pgListingService.getPendingPgListings());
		model.addAttribute("title", "Pending PG Listings");
		model.addAttribute("pendingOnly", true);
		return "admin/pg-listings";
	}

	@PostMapping("/admin/pg-listings/approve/{id}")
	public String approvePgListing(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		pgListingService.approvePgListing(id);
		redirectAttributes.addFlashAttribute("success", "PG listing approved successfully.");
		return "redirect:/admin/pg-listings";
	}

	@PostMapping("/admin/pg-listings/reject/{id}")
	public String rejectPgListing(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		pgListingService.rejectPgListing(id);
		redirectAttributes.addFlashAttribute("success", "PG listing rejected successfully.");
		return "redirect:/admin/pg-listings";
	}

	@PostMapping("/admin/users/enable/{id}")
	public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userService.enableUser(id);
		redirectAttributes.addFlashAttribute("success", "User enabled successfully.");
		return "redirect:/admin/dashboard";
	}

	@PostMapping("/admin/owners/approve/{id}")
	public String approveOwner(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userService.enableUser(id);
		redirectAttributes.addFlashAttribute("success", "PG owner approved successfully.");
		return "redirect:/admin/owners/pending";
	}

	@PostMapping("/admin/owners/reject/{id}")
	public String rejectOwner(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userService.rejectOwner(id);
		redirectAttributes.addFlashAttribute("success", "PG owner rejected.");
		return "redirect:/admin/owners/pending";
	}

	@PostMapping("/admin/users/disable/{id}")
	public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userService.disableUser(id);
		redirectAttributes.addFlashAttribute("success", "User disabled successfully.");
		return "redirect:/admin/dashboard";
	}
}
