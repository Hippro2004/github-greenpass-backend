package com.example.greenpass.v1.ReplyReport.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;

public interface ReplyReporyRepository extends JpaRepository<ReplyReport, Long> {
    List<ReplyReport> findAllByReportReportId(int reportId);
}
