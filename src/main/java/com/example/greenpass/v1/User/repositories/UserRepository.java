package com.example.greenpass.v1.User.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.User.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String token);

    Optional<User> findByPhone(String token);

    Optional<User> findByFirstname(String token);

}
