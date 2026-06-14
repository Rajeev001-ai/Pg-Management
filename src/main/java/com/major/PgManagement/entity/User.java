package com.major.pgmanagement.entity;

import com.major.pgmanagement.entity.enums.Role;
import com.major.pgmanagement.entity.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	@NotBlank(message = "Full name is required.")
	private String fullName;

	@Column(nullable = false, unique = true, length = 150)
	@NotBlank(message = "Email is required.")
	@Email(message = "Please enter a valid email address.")
	private String email;

	@Column(nullable = false)
	@NotBlank(message = "Password is required.")
	@Size(min = 6, message = "Password must be at least 6 characters.")
	private String password;

	@Column(length = 20)
	@NotBlank(message = "Phone is required.")
	private String phone;

	@Column(length = 150)
	private String pgName;

	@Column(length = 255)
	private String pgAddress;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@NotNull(message = "Role is required.")
	private Role role;

	@Column(nullable = false)
	private Boolean enabled = true;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private AccountStatus accountStatus = AccountStatus.ACTIVE;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		if (accountStatus == null) {
			accountStatus = AccountStatus.ACTIVE;
		}
		createdAt = LocalDateTime.now();
	}
}
