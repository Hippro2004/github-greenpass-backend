package com.example.greenpass.v1.Report.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.greenpass.v1.Report.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByUserUsername(String username);

    Optional<Report> findByUserUsername(String username);

    Optional<Report> findByReportId(int id);

    List<Report> findAllByParkParkIdOrderByReportIdDesc(Integer parkId);

    List<Report> findAllByOrderByReportIdDesc();

    @Query("SELECT r.park.parkId, r.status, COUNT(r) FROM Report r " +
           "WHERE (:year IS NULL OR function('YEAR', r.reportDate) = :year) " +
           "AND (:month IS NULL OR function('MONTH', r.reportDate) = :month) " +
           "GROUP BY r.park.parkId, r.status")
    List<Object[]> countReportsByParkAndFilters(@Param("year") Integer year, @Param("month") Integer month);
}
