package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.BookingRequest;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.Room;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import com.major.pgmanagement.entity.enums.RoomType;
import com.major.pgmanagement.service.BookingRequestService;
import com.major.pgmanagement.service.CloudinaryImageService;
import com.major.pgmanagement.service.PgListingService;
import com.major.pgmanagement.service.RoomService;
import com.major.pgmanagement.service.UserService;
import jakarta.validation.Valid;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OwnerController {

	private final UserService userService;
	private final PgListingService pgListingService;
	private final RoomService roomService;
	private final BookingRequestService bookingRequestService;
	private final CloudinaryImageService cloudinaryImageService;

	public OwnerController(
			UserService userService,
			PgListingService pgListingService,
			RoomService roomService,
			BookingRequestService bookingRequestService,
			CloudinaryImageService cloudinaryImageService) {
		this.userService = userService;
		this.pgListingService = pgListingService;
		this.roomService = roomService;
		this.bookingRequestService = bookingRequestService;
		this.cloudinaryImageService = cloudinaryImageService;
	}

	@GetMapping("/owner/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		List<PgListing> pgListings = pgListingService.getPgListingsByOwner(owner);
		List<BookingRequest> bookingRequests = getBookingRequestsForOwner(pgListings);

		long approvedListings = pgListings.stream()
				.filter(pg -> pg.getStatus() == PgStatus.APPROVED)
				.count();
		long pendingListings = pgListings.stream()
				.filter(pg -> pg.getStatus() == PgStatus.PENDING)
				.count();
		int totalRooms = pgListings.stream()
				.mapToInt(pg -> roomService.getRoomsByPgListing(pg.getId()).size())
				.sum();

		model.addAttribute("totalPgListings", pgListings.size());
		model.addAttribute("approvedListings", approvedListings);
		model.addAttribute("pendingListings", pendingListings);
		model.addAttribute("totalRooms", totalRooms);
		model.addAttribute("totalBookingRequests", bookingRequests.size());
		return "owner/dashboard";
	}

	@GetMapping("/owner/pgs")
	public String pgListings(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		model.addAttribute("pgListings", pgListingService.getPgListingsByOwner(owner));
		return "owner/pg-list";
	}

	@GetMapping("/owner/pgs/add")
	public String addPgForm(Model model) {
		model.addAttribute("pgListing", new PgListing());
		model.addAttribute("pgTypes", PgType.values());
		model.addAttribute("isEdit", false);
		return "owner/pg-form";
	}

	@PostMapping("/owner/pgs/add")
	public String addPg(
			@Valid @ModelAttribute PgListing pgListing,
			BindingResult bindingResult,
			@RequestParam(value = "image", required = false) MultipartFile image,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			addPgFormAttributes(model, false);
			return "owner/pg-form";
		}
		User owner = getLoggedInOwner(authentication);
		String imageUrl = cloudinaryImageService.uploadPgImage(image);
		pgListing.setImageUrl(imageUrl);
		pgListingService.createPgListing(pgListing, owner);
		redirectAttributes.addFlashAttribute("success", "PG listing submitted for admin approval.");
		return "redirect:/owner/pgs";
	}

	@GetMapping("/owner/pgs/edit/{id}")
	public String editPgForm(@PathVariable Long id, Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		PgListing pgListing = getOwnedPgListing(id, owner);
		model.addAttribute("pgListing", pgListing);
		model.addAttribute("pgTypes", PgType.values());
		model.addAttribute("isEdit", true);
		return "owner/pg-form";
	}

	@PostMapping("/owner/pgs/edit/{id}")
	public String editPg(
			@PathVariable Long id,
			@Valid @ModelAttribute PgListing updatedPg,
			BindingResult bindingResult,
			@RequestParam(value = "image", required = false) MultipartFile image,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		PgListing existingPg = getOwnedPgListing(id, owner);
		if (bindingResult.hasErrors()) {
			updatedPg.setId(id);
			updatedPg.setImageUrl(existingPg.getImageUrl());
			addPgFormAttributes(model, true);
			return "owner/pg-form";
		}
		String imageUrl = cloudinaryImageService.uploadPgImage(image);
		updatedPg.setImageUrl(imageUrl != null ? imageUrl : existingPg.getImageUrl());
		pgListingService.updatePgListing(id, updatedPg);
		redirectAttributes.addFlashAttribute("success", "PG listing updated successfully.");
		return "redirect:/owner/pgs";
	}

	@GetMapping("/owner/rooms/{pgId}")
	public String rooms(@PathVariable Long pgId, Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		PgListing pgListing = getOwnedPgListing(pgId, owner);
		model.addAttribute("pgListing", pgListing);
		model.addAttribute("rooms", roomService.getRoomsByPgListing(pgId));
		return "owner/room-list";
	}

	@GetMapping("/owner/rooms/add/{pgId}")
	public String addRoomForm(@PathVariable Long pgId, Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		PgListing pgListing = getOwnedPgListing(pgId, owner);
		model.addAttribute("pgListing", pgListing);
		model.addAttribute("room", new Room());
		model.addAttribute("roomTypes", RoomType.values());
		return "owner/room-form";
	}

	@PostMapping("/owner/rooms/add/{pgId}")
	public String addRoom(
			@PathVariable Long pgId,
			@Valid @ModelAttribute Room room,
			BindingResult bindingResult,
			Authentication authentication,
			Model model,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		PgListing pgListing = getOwnedPgListing(pgId, owner);
		if (bindingResult.hasErrors()) {
			addRoomFormAttributes(model, pgListing);
			return "owner/room-form";
		}
		roomService.addRoom(room, pgId);
		redirectAttributes.addFlashAttribute("success", "Room added successfully.");
		return "redirect:/owner/rooms/" + pgId;
	}

	@GetMapping("/owner/bookings")
	public String bookingRequests(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		List<PgListing> pgListings = pgListingService.getPgListingsByOwner(owner);
		model.addAttribute("bookingRequests", getBookingRequestsForOwner(pgListings));
		return "owner/booking-requests";
	}

	@PostMapping("/owner/bookings/accept/{id}")
	public String acceptBooking(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		getOwnedBookingRequest(id, owner);
		bookingRequestService.acceptRequest(id);
		redirectAttributes.addFlashAttribute("success", "Booking request accepted.");
		return "redirect:/owner/bookings";
	}

	@PostMapping("/owner/bookings/reject/{id}")
	public String rejectBooking(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		getOwnedBookingRequest(id, owner);
		bookingRequestService.rejectRequest(id);
		redirectAttributes.addFlashAttribute("success", "Booking request rejected.");
		return "redirect:/owner/bookings";
	}

	@GetMapping("/owner/profile")
	public String profile(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		model.addAttribute("user", owner);
		return "owner/profile";
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

	private BookingRequest getOwnedBookingRequest(Long requestId, User owner) {
		BookingRequest bookingRequest = bookingRequestService.getBookingRequestById(requestId);
		if (bookingRequest.getPgListing() == null
				|| bookingRequest.getPgListing().getOwner() == null
				|| !bookingRequest.getPgListing().getOwner().getId().equals(owner.getId())) {
			throw new RuntimeException("You are not allowed to manage this booking request.");
		}
		return bookingRequest;
	}

	private List<BookingRequest> getBookingRequestsForOwner(List<PgListing> pgListings) {
		List<BookingRequest> bookingRequests = new ArrayList<>();
		for (PgListing pgListing : pgListings) {
			bookingRequests.addAll(bookingRequestService.getRequestsByPgListing(pgListing.getId()));
		}
		return bookingRequests;
	}

	private void addPgFormAttributes(Model model, boolean isEdit) {
		model.addAttribute("pgTypes", PgType.values());
		model.addAttribute("isEdit", isEdit);
	}

	private void addRoomFormAttributes(Model model, PgListing pgListing) {
		model.addAttribute("pgListing", pgListing);
		model.addAttribute("roomTypes", RoomType.values());
	}

}
