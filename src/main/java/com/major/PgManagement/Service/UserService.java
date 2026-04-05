package com.major.PgManagement.Service;

import java.util.Optional;

import com.major.PgManagement.Entities.User;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
}

