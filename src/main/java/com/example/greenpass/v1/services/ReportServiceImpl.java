package com.example.greenpass.v1.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.dtos.AddReportDto;
import com.example.greenpass.v1.entities.Report;
import com.example.greenpass.v1.repositories.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Override
    public List<Report> getAllByUsername(String username) {
        return reportRepository.findAllByUserUsername(username);
    }

    @Override
    public Report getByUsername(String name) {
        return reportRepository.findByUserUsername(name).orElse(null);
    }

    @Override
    public void addReport(AddReportDto addReportDto) {
        Report addReport = Report.builder().name(addReportDto.getName())
                .description(addReportDto.getDescription())
                .reportDate(LocalDate.now())
                .image(addReportDto.getImage())
                .build();
        reportRepository.save(addReport);
    }

}
