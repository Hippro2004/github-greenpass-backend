package com.example.greenpass.v1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByUserUsername(String username);

    Optional<Report> findByUserUsername(String username);
}
