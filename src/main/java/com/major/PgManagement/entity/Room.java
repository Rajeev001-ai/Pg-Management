package com.major.pgmanagement.entity;

import com.major.pgmanagement.entity.enums.RoomType;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "Room number is required.")
	private String roomNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@NotNull(message = "Room type is required.")
	private RoomType roomType;

	@Column(nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Rent amount is required.")
	@Positive(message = "Rent amount must be positive.")
	private BigDecimal rentAmount;

	@Column(nullable = false)
	@NotNull(message = "Total beds is required.")
	@Min(value = 1, message = "Total beds must be at least 1.")
	private Integer totalBeds;

	@Column(nullable = false)
	@NotNull(message = "Available beds is required.")
	@Min(value = 0, message = "Available beds cannot be negative.")
	private Integer availableBeds;

	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pg_listing_id", nullable = false)
	private PgListing pgListing;
}
