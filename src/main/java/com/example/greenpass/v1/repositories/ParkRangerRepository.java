package com.example.greenpass.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.greenpass.v1.entities.ParkRanger;

@Repository
public interface ParkRangerRepository extends JpaRepository<ParkRanger, String> {
    ParkRanger findByUsername(String username);
}
