package com.example.greenpass.v1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.greenpass.v1.entities.Stamp;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Integer> {
    List<Stamp> findAllByUserUsername(String username);

    Stamp findByUserUsernameAndParkParkId(String username, int parkId);
}
