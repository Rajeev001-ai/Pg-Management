package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.AccountStatus;
import com.major.pgmanagement.entity.enums.Role;
import com.major.pgmanagement.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public User registerUser(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("User already exists with email: " + user.getEmail());
		}
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getAllOwners() {
		return userRepository.findByRole(Role.OWNER);
	}

	@Override
	public List<User> getPendingOwners() {
		return userRepository.findByRoleAndAccountStatus(Role.OWNER, AccountStatus.PENDING_APPROVAL);
	}

	@Override
	public List<User> getAllTenants() {
		return userRepository.findByRole(Role.TENANT);
	}

	@Override
	public User getUserById(Long userId) {
		return getUserOrThrow(userId);
	}

	@Override
	@Transactional
	public User enableUser(Long userId) {
		User user = getUserOrThrow(userId);
		user.setEnabled(true);
		user.setAccountStatus(AccountStatus.ACTIVE);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User disableUser(Long userId) {
		User user = getUserOrThrow(userId);
		user.setEnabled(false);
		user.setAccountStatus(AccountStatus.DISABLED);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User rejectOwner(Long userId) {
		User user = getUserOrThrow(userId);
		if (user.getRole() != Role.OWNER) {
			throw new RuntimeException("Only PG owner accounts can be rejected.");
		}
		user.setEnabled(false);
		user.setAccountStatus(AccountStatus.REJECTED);
		return userRepository.save(user);
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
	}
}
