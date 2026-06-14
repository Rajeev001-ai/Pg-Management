package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.enums.BookingStatus;
import com.major.pgmanagement.entity.enums.ComplaintStatus;
import com.major.pgmanagement.entity.enums.RentStatus;
import com.major.pgmanagement.service.BookingRequestService;
import com.major.pgmanagement.service.ComplaintService;
import com.major.pgmanagement.service.RentPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminMonitoringController {

	private final BookingRequestService bookingRequestService;
	private final ComplaintService complaintService;
	private final RentPaymentService rentPaymentService;

	public AdminMonitoringController(
			BookingRequestService bookingRequestService,
			ComplaintService complaintService,
			RentPaymentService rentPaymentService) {
		this.bookingRequestService = bookingRequestService;
		this.complaintService = complaintService;
		this.rentPaymentService = rentPaymentService;
	}

	@GetMapping("/admin/bookings")
	public String bookings(@RequestParam(required = false) BookingStatus status, Model model) {
		model.addAttribute("bookingRequests",
				status == null ? bookingRequestService.getAllRequests() : bookingRequestService.getRequestsByStatus(status));
		model.addAttribute("statuses", BookingStatus.values());
		model.addAttribute("selectedStatus", status);
		return "admin/bookings";
	}

	@GetMapping("/admin/complaints")
	public String complaints(@RequestParam(required = false) ComplaintStatus status, Model model) {
		model.addAttribute("complaints",
				status == null ? complaintService.getAllComplaints() : complaintService.getComplaintsByStatus(status));
		model.addAttribute("statuses", ComplaintStatus.values());
		model.addAttribute("selectedStatus", status);
		return "admin/complaints";
	}

	@GetMapping("/admin/rent-payments")
	public String rentPayments(@RequestParam(required = false) RentStatus status, Model model) {
		model.addAttribute("rentPayments",
				status == null ? rentPaymentService.getAllRentPayments() : rentPaymentService.getRentPaymentsByStatus(status));
		model.addAttribute("statuses", RentStatus.values());
		model.addAttribute("selectedStatus", status);
		return "admin/rent-payments";
	}
}
