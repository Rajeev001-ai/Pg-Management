package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.service.PgListingService;
import com.major.pgmanagement.service.RentPaymentService;
import com.major.pgmanagement.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OwnerRentController {

	private final UserService userService;
	private final PgListingService pgListingService;
	private final RentPaymentService rentPaymentService;

	public OwnerRentController(
			UserService userService,
			PgListingService pgListingService,
			RentPaymentService rentPaymentService) {
		this.userService = userService;
		this.pgListingService = pgListingService;
		this.rentPaymentService = rentPaymentService;
	}

	@GetMapping("/owner/rent-payments")
	public String rentPayments(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		model.addAttribute("rentPayments", rentPaymentService.getRentPaymentsByOwner(owner));
		model.addAttribute("selectedPg", null);
		return "owner/rent-payments";
	}

	@GetMapping("/owner/rent-payments/pg/{pgId}")
	public String rentPaymentsByPg(@PathVariable Long pgId, Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		PgListing pgListing = getOwnedPgListing(pgId, owner);
		model.addAttribute("rentPayments", rentPaymentService.getRentPaymentsByPgListing(pgId));
		model.addAttribute("selectedPg", pgListing);
		return "owner/rent-payments";
	}

	@PostMapping("/owner/rent-payments/mark-paid/{id}")
	public String markPaid(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		getOwnedRentPayment(id, owner);
		rentPaymentService.markAsPaid(id);
		redirectAttributes.addFlashAttribute("success", "Rent payment marked as paid.");
		return "redirect:/owner/rent-payments";
	}

	@PostMapping("/owner/rent-payments/mark-overdue/{id}")
	public String markOverdue(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		getOwnedRentPayment(id, owner);
		rentPaymentService.markAsOverdue(id);
		redirectAttributes.addFlashAttribute("success", "Rent payment marked as overdue.");
		return "redirect:/owner/rent-payments";
	}

	@GetMapping("/owner/rent-payments/create")
	public String createRentPaymentForm(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		LocalDate today = LocalDate.now();
		model.addAttribute("pgListings", pgListingService.getPgListingsByOwner(owner));
		model.addAttribute("tenants", userService.getAllTenants());
		model.addAttribute("currentMonth", today.getMonth().name());
		model.addAttribute("currentYear", today.getYear());
		return "owner/rent-payment-form";
	}

	@PostMapping("/owner/rent-payments/create")
	public String createRentPayment(
			@RequestParam Long tenantId,
			@RequestParam Long pgListingId,
			@RequestParam BigDecimal amount,
			@RequestParam String month,
			@RequestParam Integer year,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		PgListing pgListing = getOwnedPgListing(pgListingId, owner);
		User tenant = userService.getUserById(tenantId);

		RentPayment rentPayment = new RentPayment();
		rentPayment.setTenant(tenant);
		rentPayment.setPgListing(pgListing);
		rentPayment.setAmount(amount);
		rentPayment.setMonth(month);
		rentPayment.setYear(year);
		rentPaymentService.createRentPayment(rentPayment);

		redirectAttributes.addFlashAttribute("success", "Manual rent payment created.");
		return "redirect:/owner/rent-payments";
	}

	private User getLoggedInOwner(Authentication authentication) {
		return userService.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Logged-in owner not found."));
	}

	private PgListing getOwnedPgListing(Long pgListingId, User owner) {
		PgListing pgListing = pgListingService.getPgListingById(pgListingId);
		if (pgListing.getOwner() == null || !pgListing.getOwner().getId().equals(owner.getId())) {
			throw new RuntimeException("You are not allowed to manage this PG listing.");
		}
		return pgListing;
	}

	private RentPayment getOwnedRentPayment(Long paymentId, User owner) {
		RentPayment rentPayment = rentPaymentService.getRentPaymentById(paymentId);
		if (rentPayment.getPgListing() == null
				|| rentPayment.getPgListing().getOwner() == null
				|| !rentPayment.getPgListing().getOwner().getId().equals(owner.getId())) {
			throw new RuntimeException("You are not allowed to manage this rent payment.");
		}
		return rentPayment;
	}
}
