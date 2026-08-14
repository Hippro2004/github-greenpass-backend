package com.example.greenpass.v1.Report.services;

import java.util.List;

import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.entities.Report;

public interface ReportService {
    List<Report> getAllByUsername(String username);

    Report getByUsername(String name);

    void addReport(AddReportDto addReportDto);

}
