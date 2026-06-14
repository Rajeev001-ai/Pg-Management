package com.major.pgmanagement.entity;

import com.major.pgmanagement.entity.enums.RentStatus;
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
@Table(name = "rent_payments")
public class RentPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant_id", nullable = false)
	private User tenant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pg_listing_id", nullable = false)
	private PgListing pgListing;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 20)
	private String month;

	@Column(nullable = false)
	private Integer year;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RentStatus status = RentStatus.PENDING;

	private LocalDateTime paymentDate;
}
