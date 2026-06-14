package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.Complaint;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.service.ComplaintService;
import com.major.pgmanagement.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OwnerComplaintController {

	private final UserService userService;
	private final ComplaintService complaintService;

	public OwnerComplaintController(UserService userService, ComplaintService complaintService) {
		this.userService = userService;
		this.complaintService = complaintService;
	}

	@GetMapping("/owner/complaints")
	public String complaints(Authentication authentication, Model model) {
		User owner = getLoggedInOwner(authentication);
		model.addAttribute("complaints", complaintService.getComplaintsByOwner(owner));
		return "owner/complaints";
	}

	@PostMapping("/owner/complaints/in-progress/{id}")
	public String markInProgress(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		getOwnedComplaint(id, owner);
		complaintService.markInProgress(id);
		redirectAttributes.addFlashAttribute("success", "Complaint marked as in progress.");
		return "redirect:/owner/complaints";
	}

	@PostMapping("/owner/complaints/resolved/{id}")
	public String markResolved(
			@PathVariable Long id,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User owner = getLoggedInOwner(authentication);
		getOwnedComplaint(id, owner);
		complaintService.markResolved(id);
		redirectAttributes.addFlashAttribute("success", "Complaint marked as resolved.");
		return "redirect:/owner/complaints";
	}

	private User getLoggedInOwner(Authentication authentication) {
		return userService.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Logged-in owner not found."));
	}

	private Complaint getOwnedComplaint(Long complaintId, User owner) {
		Complaint complaint = complaintService.getComplaintById(complaintId);
		if (complaint.getPgListing() == null
				|| complaint.getPgListing().getOwner() == null
				|| !complaint.getPgListing().getOwner().getId().equals(owner.getId())) {
			throw new RuntimeException("You are not allowed to manage this complaint.");
		}
		return complaint;
	}
}
