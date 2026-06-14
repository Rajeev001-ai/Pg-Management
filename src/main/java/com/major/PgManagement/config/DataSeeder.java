package com.major.pgmanagement.config;

import com.major.pgmanagement.entity.BookingRequest;
import com.major.pgmanagement.entity.Complaint;
import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.RentPayment;
import com.major.pgmanagement.entity.Room;
import com.major.pgmanagement.entity.TenantAssignment;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.BookingStatus;
import com.major.pgmanagement.entity.enums.ComplaintStatus;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import com.major.pgmanagement.entity.enums.RentStatus;
import com.major.pgmanagement.entity.enums.Role;
import com.major.pgmanagement.entity.enums.RoomType;
import com.major.pgmanagement.repository.BookingRequestRepository;
import com.major.pgmanagement.repository.ComplaintRepository;
import com.major.pgmanagement.repository.PgListingRepository;
import com.major.pgmanagement.repository.RentPaymentRepository;
import com.major.pgmanagement.repository.RoomRepository;
import com.major.pgmanagement.repository.TenantAssignmentRepository;
import com.major.pgmanagement.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@ConditionalOnProperty(name = "app.data-seeder.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PgListingRepository pgListingRepository;
	private final RoomRepository roomRepository;
	private final BookingRequestRepository bookingRequestRepository;
	private final ComplaintRepository complaintRepository;
	private final RentPaymentRepository rentPaymentRepository;
	private final TenantAssignmentRepository tenantAssignmentRepository;
	private final PasswordEncoder passwordEncoder;
	private final String adminEmail;
	private final String adminPassword;

	public DataSeeder(
			UserRepository userRepository,
			PgListingRepository pgListingRepository,
			RoomRepository roomRepository,
			BookingRequestRepository bookingRequestRepository,
			ComplaintRepository complaintRepository,
			RentPaymentRepository rentPaymentRepository,
			TenantAssignmentRepository tenantAssignmentRepository,
			PasswordEncoder passwordEncoder,
			@Value("${app.seed.admin.email}") String adminEmail,
			@Value("${app.seed.admin.password}") String adminPassword) {
		this.userRepository = userRepository;
		this.pgListingRepository = pgListingRepository;
		this.roomRepository = roomRepository;
		this.bookingRequestRepository = bookingRequestRepository;
		this.complaintRepository = complaintRepository;
		this.rentPaymentRepository = rentPaymentRepository;
		this.tenantAssignmentRepository = tenantAssignmentRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
	}

	@Override
	public void run(String... args) {
		if (userRepository.count() > 0) {
			return;
		}

		createUser("System Admin", adminEmail, adminPassword, "9000000001", Role.ADMIN);
		User owner = createUser("Rahul Sharma", "owner@gmail.com", "owner123", "9000000002", Role.OWNER);
		User tenant = createUser("Priya Verma", "tenant@gmail.com", "tenant123", "9000000003", Role.TENANT);

		PgListing comfortStay = createPgListing(
				"Comfort Stay PG",
				"Clean and secure PG near metro station with homely food and Wi-Fi.",
				"12 Green Park Road",
				"Delhi",
				"Green Park",
				PgType.CO_LIVING,
				new BigDecimal("9500"),
				"Wi-Fi, Meals, Laundry, CCTV, Power Backup",
				"No smoking inside rooms. Visitors allowed only in common area.",
				PgStatus.APPROVED,
				owner);

		PgListing sunriseHomes = createPgListing(
				"Sunrise Homes PG",
				"Well-maintained boys PG close to colleges and office hubs.",
				"45 Lake View Street",
				"Bengaluru",
				"BTM Layout",
				PgType.BOYS,
				new BigDecimal("8500"),
				"Wi-Fi, Housekeeping, Hot Water, Parking",
				"Rent due by 5th of every month. Quiet hours after 10 PM.",
				PgStatus.APPROVED,
				owner);

		createPgListing(
				"Blue Nest Girls PG",
				"Girls PG with spacious rooms and 24x7 security.",
				"21 Rose Avenue",
				"Pune",
				"Hinjewadi",
				PgType.GIRLS,
				new BigDecimal("11000"),
				"Wi-Fi, Meals, Security, Housekeeping",
				"ID proof required for visitors.",
				PgStatus.PENDING,
				owner);

		Room comfortSingle = createRoom("101", RoomType.SINGLE, new BigDecimal("12000"), 1, 0, comfortStay);
		createRoom("102", RoomType.DOUBLE, new BigDecimal("9500"), 2, 2, comfortStay);
		Room sunriseDouble = createRoom("201", RoomType.DOUBLE, new BigDecimal("8500"), 2, 1, sunriseHomes);

		createBookingRequest(tenant, comfortStay, comfortSingle);
		createTenantAssignment(tenant, comfortStay, comfortSingle);
		createComplaint(tenant, comfortStay);
		createRentPayment(tenant, comfortStay, comfortSingle.getRentAmount());

		// Keeps the sample rooms visibly useful in both approved PGs.
		sunriseDouble.setDescription("Shared room with study table and wardrobe.");
		roomRepository.save(sunriseDouble);
	}

	private User createUser(String fullName, String email, String password, String phone, Role role) {
		User user = new User();
		user.setFullName(fullName);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setPhone(phone);
		user.setRole(role);
		user.setEnabled(true);
		return userRepository.save(user);
	}

	private PgListing createPgListing(
			String name,
			String description,
			String address,
			String city,
			String area,
			PgType pgType,
			BigDecimal rent,
			String facilities,
			String rules,
			PgStatus status,
			User owner) {
		PgListing pgListing = new PgListing();
		pgListing.setPgName(name);
		pgListing.setDescription(description);
		pgListing.setAddress(address);
		pgListing.setCity(city);
		pgListing.setArea(area);
		pgListing.setPgType(pgType);
		pgListing.setMonthlyRentStartingFrom(rent);
		pgListing.setFacilities(facilities);
		pgListing.setRules(rules);
		pgListing.setStatus(status);
		pgListing.setOwner(owner);
		return pgListingRepository.save(pgListing);
	}

	private Room createRoom(String roomNumber, RoomType roomType, BigDecimal rent, int totalBeds, int availableBeds, PgListing pgListing) {
		Room room = new Room();
		room.setRoomNumber(roomNumber);
		room.setRoomType(roomType);
		room.setRentAmount(rent);
		room.setTotalBeds(totalBeds);
		room.setAvailableBeds(availableBeds);
		room.setDescription("Comfortable room with basic furniture and ventilation.");
		room.setPgListing(pgListing);
		return roomRepository.save(room);
	}

	private void createBookingRequest(User tenant, PgListing pgListing, Room room) {
		BookingRequest bookingRequest = new BookingRequest();
		bookingRequest.setTenant(tenant);
		bookingRequest.setPgListing(pgListing);
		bookingRequest.setRoom(room);
		bookingRequest.setMessage("I visited the PG and would like to confirm this room.");
		bookingRequest.setStatus(BookingStatus.ACCEPTED);
		bookingRequestRepository.save(bookingRequest);
	}

	private void createTenantAssignment(User tenant, PgListing pgListing, Room room) {
		TenantAssignment assignment = new TenantAssignment();
		assignment.setTenant(tenant);
		assignment.setPgListing(pgListing);
		assignment.setRoom(room);
		assignment.setStartDate(LocalDateTime.now().minusDays(3));
		assignment.setActive(true);
		tenantAssignmentRepository.save(assignment);
	}

	private void createComplaint(User tenant, PgListing pgListing) {
		Complaint complaint = new Complaint();
		complaint.setTenant(tenant);
		complaint.setPgListing(pgListing);
		complaint.setTitle("Wi-Fi connectivity issue");
		complaint.setDescription("The Wi-Fi speed has been low in the evening. Please check the router.");
		complaint.setStatus(ComplaintStatus.OPEN);
		complaintRepository.save(complaint);
	}

	private void createRentPayment(User tenant, PgListing pgListing, BigDecimal amount) {
		LocalDate today = LocalDate.now();
		RentPayment rentPayment = new RentPayment();
		rentPayment.setTenant(tenant);
		rentPayment.setPgListing(pgListing);
		rentPayment.setAmount(amount);
		rentPayment.setMonth(today.getMonth().name());
		rentPayment.setYear(today.getYear());
		rentPayment.setStatus(RentStatus.PENDING);
		rentPaymentRepository.save(rentPayment);
	}
}
