package com.major.pgmanagement.service;

import com.major.pgmanagement.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

	User registerUser(User user);

	Optional<User> findByEmail(String email);

	List<User> getAllUsers();

	List<User> getAllOwners();

	List<User> getPendingOwners();

	List<User> getAllTenants();

	User getUserById(Long userId);

	User enableUser(Long userId);

	User disableUser(Long userId);

	User rejectOwner(Long userId);
}
