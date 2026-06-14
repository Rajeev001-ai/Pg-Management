package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.AccountStatus;
import com.major.pgmanagement.entity.enums.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	List<User> findByRole(Role role);

	List<User> findByRoleAndAccountStatus(Role role, AccountStatus accountStatus);
}
