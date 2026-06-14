package com.major.pgmanagement.entity;

import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
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
@Table(name = "pg_listings")
public class PgListing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	@NotBlank(message = "PG name is required.")
	private String pgName;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, length = 255)
	@NotBlank(message = "Address is required.")
	private String address;

	@Column(nullable = false, length = 100)
	@NotBlank(message = "City is required.")
	private String city;

	@Column(nullable = false, length = 100)
	@NotBlank(message = "Area is required.")
	private String area;

	private Double latitude;

	private Double longitude;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@NotNull(message = "PG type is required.")
	private PgType pgType;

	@Column(nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Starting rent is required.")
	@Positive(message = "Starting rent must be positive.")
	private BigDecimal monthlyRentStartingFrom;

	@Column(columnDefinition = "TEXT")
	private String facilities;

	@Column(columnDefinition = "TEXT")
	private String rules;

	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private PgStatus status = PgStatus.PENDING;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
