package com.major.pgmanagement.config;

import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.AccountStatus;
import com.major.pgmanagement.entity.enums.Role;
import com.major.pgmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Order(2)
@ConditionalOnProperty(name = "app.admin.bootstrap.enabled", havingValue = "true", matchIfMissing = true)
public class AdminBootstrap implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final String adminEmail;
	private final String adminPassword;

	public AdminBootstrap(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			@Value("${app.seed.admin.email}") String adminEmail,
			@Value("${app.seed.admin.password}") String adminPassword) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
	}

	@Override
	public void run(String... args) {
		if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminPassword)) {
			throw new IllegalStateException("Admin email and password must be configured before startup.");
		}

		if (userRepository.existsByEmail(adminEmail)) {
			return;
		}

		User admin = new User();
		admin.setFullName("System Admin");
		admin.setEmail(adminEmail);
		admin.setPassword(passwordEncoder.encode(adminPassword));
		admin.setPhone("9000000001");
		admin.setRole(Role.ADMIN);
		admin.setEnabled(true);
		admin.setAccountStatus(AccountStatus.ACTIVE);
		userRepository.save(admin);
	}
}
