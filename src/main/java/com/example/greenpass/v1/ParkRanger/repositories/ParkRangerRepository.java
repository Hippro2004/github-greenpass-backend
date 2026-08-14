package com.example.greenpass.v1.ParkRanger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;

public interface ParkRangerRepository extends JpaRepository<ParkRanger, String> {
    ParkRanger findByUsername(String username);
}
