package com.example.greenpass.v1.ReportType.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.ReportType.entities.ReportType;

public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {
    boolean existsByTypeName(String typeName);
    Optional<ReportType> findByTypeName(String typeName);
}

