package com.example.greenpass.v1.ReportType.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.ReportType.dtos.ReportTypeResponse;
import com.example.greenpass.v1.ReportType.entities.ReportType;
import com.example.greenpass.v1.ReportType.repositories.ReportTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporyTypeServicImpl implements ReporyTypeService {

    private final ReportTypeRepository reportTypeRepository;

    @Override
    public void saveType(ReportType reportType) {
        reportTypeRepository.save(reportType);
    }

    @Override
    public List<ReportTypeResponse> getAllType() {
        List<ReportType> reportTypes = reportTypeRepository.findAll();
        if (reportTypes.isEmpty()) {
            return null;
        }
        return reportTypes.stream()
                .map(reportType -> new ReportTypeResponse(reportType.getTypeName()))
                .toList();
    }

    @Override
    public ReportType getTypeByName(String typeName) {
        return reportTypeRepository.findByTypeName(typeName).orElse(null);
    }

}
