package com.example.greenpass.v1.Report.services;

import java.util.List;

import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.entities.Report;

public interface ReportService {
    List<ReportResponse> getAllByUsername(String username);

    Report getByUsername(String name);

    void addReport(AddReportDto addReportDto, String username);

    Report getByReportId(int id);

    List<ReportResponse> getAllReports();

    List<ReportResponse> getReportsByParkId(int parkId);

    List<ReportResponse> getReportsByRangerUsername(String rangerUsername);

    ReportResponse updateReportStatus(int reportId, String status, String rangerUsername);
}

