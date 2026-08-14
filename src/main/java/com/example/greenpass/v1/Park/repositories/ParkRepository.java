package com.example.greenpass.v1.Park.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Park.entities.Park;

public interface ParkRepository extends JpaRepository<Park, Integer> {
    List<Park> findByNameContainingIgnoreCase(String keyword);
}
