package com.major.pgmanagement.entity;

import com.major.pgmanagement.entity.enums.ComplaintStatus;
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
@Table(name = "complaints")
public class Complaint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant_id", nullable = false)
	private User tenant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pg_listing_id", nullable = false)
	private PgListing pgListing;

	@Column(nullable = false, length = 150)
	@NotBlank(message = "Title is required.")
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	@NotBlank(message = "Description is required.")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ComplaintStatus status = ComplaintStatus.OPEN;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
