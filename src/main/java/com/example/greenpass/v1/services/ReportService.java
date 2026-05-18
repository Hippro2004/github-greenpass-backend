package com.example.greenpass.v1.services;

import java.util.List;

import com.example.greenpass.v1.dtos.AddReportDto;
import com.example.greenpass.v1.entities.Report;

public interface ReportService {
    List<Report> getAllByUsername(String username);

    Report getByUsername(String name);

    void addReport(AddReportDto addReportDto);

}
