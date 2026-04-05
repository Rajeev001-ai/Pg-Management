package com.major.PgManagement.Service;

import org.springframework.stereotype.Service;

import com.major.PgManagement.Entities.User;
import com.major.PgManagement.Repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User saveUser(User user) {
        return repo.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}

