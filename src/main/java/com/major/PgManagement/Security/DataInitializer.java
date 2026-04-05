package com.major.PgManagement.Security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.major.PgManagement.Entities.Role;
import com.major.PgManagement.Entities.User;
import com.major.PgManagement.Repository.UserRepository;

@Configuration
public class DataInitializer {

@Bean
public CommandLineRunner createDefaultUsers(UserRepository repo, BCryptPasswordEncoder encoder) {
    return args -> {

        if(repo.findByUsername("owner@gmail.com").isEmpty()) {   // <-- isEmpty() instead of null
            User owner = new User();
            owner.setUsername("owner@gmail.com");
            owner.setPassword(encoder.encode("owner123"));
            owner.setRole(Role.OWNER);
            repo.save(owner);
            System.out.println(">> Default OWNER created");
        }

        if(repo.findByUsername("tenant@gmail.com").isEmpty()) {  // <-- isEmpty() instead of null
            User tenant = new User();
            tenant.setUsername("tenant@gmail.com");
            tenant.setPassword(encoder.encode("tenant123"));
            tenant.setRole(Role.TENANT);
            repo.save(tenant);
            System.out.println(">> Default TENANT created");
        }
    };
}

}