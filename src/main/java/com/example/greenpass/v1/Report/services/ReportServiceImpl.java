package com.example.greenpass.v1.Report.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.Report.repositories.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Override
    public List<ReportResponse> getAllByUsername(String username) {
        return reportRepository.findAllByUserUsername(username).stream()
                .map(r -> new ReportResponse(r.getName(), r.getDescription(), r.getStatus())).toList();

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

    @Override
    public Report getByReportId(Long id) {
        return reportRepository.findByReportId(id).orElse(null);
    }

}
