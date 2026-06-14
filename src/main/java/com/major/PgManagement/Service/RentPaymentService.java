package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.RentStatus;
import java.util.List;

public interface RentPaymentService {

	RentPayment createRentPayment(RentPayment rentPayment);

	List<RentPayment> getRentPaymentsByTenant(User tenant);

	List<RentPayment> getRentPaymentsByPgListing(Long pgListingId);

	List<RentPayment> getAllRentPayments();

	List<RentPayment> getRentPaymentsByStatus(RentStatus status);

	List<RentPayment> getRentPaymentsByOwner(User owner);

	RentPayment getRentPaymentById(Long paymentId);

	RentPayment markAsPaid(Long paymentId);

	RentPayment markAsOverdue(Long paymentId);
}
