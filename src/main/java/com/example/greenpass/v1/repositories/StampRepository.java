package com.example.greenpass.v1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.entities.Stamp;

public interface StampRepository extends JpaRepository<Stamp, Integer> {
    List<Stamp> findAllByUserUsername(String username);

    List<Stamp> findAllByUserUsernameAndParkParkId(String username, int parkId);
}
