package com.major.pgmanagement.entity;

import com.major.pgmanagement.entity.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_requests")
public class BookingRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant_id", nullable = false)
	private User tenant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pg_listing_id", nullable = false)
	private PgListing pgListing;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@Column(columnDefinition = "TEXT")
	@Size(max = 500, message = "Message cannot be longer than 500 characters.")
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private BookingStatus status = BookingStatus.PENDING;

	@Column(nullable = false, updatable = false)
	private LocalDateTime requestDate;

	@PrePersist
	void onCreate() {
		requestDate = LocalDateTime.now();
	}
}
