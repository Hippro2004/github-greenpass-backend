package com.example.greenpass.v1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.greenpass.v1.entities.Park;

@Repository
public interface ParkRepository extends JpaRepository<Park, Integer> {
    List<Park> findByNameContainingIgnoreCase(String keyword);
}
