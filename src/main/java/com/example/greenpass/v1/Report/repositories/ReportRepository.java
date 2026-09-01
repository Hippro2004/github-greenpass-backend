package com.example.greenpass.v1.Report.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.Report.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByUserUsername(String username);

    Optional<Report> findByUserUsername(String username);

    Optional<Report> findByReportId(int id);

    List<Report> findAllByParkParkIdOrderByReportIdDesc(Integer parkId);

    List<Report> findAllByOrderByReportIdDesc();
}

