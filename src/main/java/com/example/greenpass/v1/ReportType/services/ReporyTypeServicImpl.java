package com.example.greenpass.v1.ReportType.services;

import java.util.List;

import org.springframework.stereotype.Service;

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
    public List<ReportType> getAllType() {
        return reportTypeRepository.findAll();
    }

}
