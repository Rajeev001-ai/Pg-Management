package com.major.pgmanagement;

import com.major.pgmanagement.repository.BookingRequestRepository;
import com.major.pgmanagement.repository.ComplaintRepository;
import com.major.pgmanagement.repository.PgListingRepository;
import com.major.pgmanagement.repository.RentPaymentRepository;
import com.major.pgmanagement.repository.RoomRepository;
import com.major.pgmanagement.repository.TenantAssignmentRepository;
import com.major.pgmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
				+ "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
		"app.data-seeder.enabled=false"
})
class PgManagementApplicationTests {

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private PgListingRepository pgListingRepository;

	@MockitoBean
	private RoomRepository roomRepository;

	@MockitoBean
	private BookingRequestRepository bookingRequestRepository;

	@MockitoBean
	private RentPaymentRepository rentPaymentRepository;

	@MockitoBean
	private ComplaintRepository complaintRepository;

	@MockitoBean
	private TenantAssignmentRepository tenantAssignmentRepository;

	@Test
	void contextLoads() {
	}
}
