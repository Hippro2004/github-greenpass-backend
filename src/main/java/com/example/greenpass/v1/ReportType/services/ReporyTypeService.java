package com.example.greenpass.v1.ReportType.services;

import java.util.List;

import com.example.greenpass.v1.ReportType.entities.ReportType;

public interface ReporyTypeService {
    void saveType(ReportType reportType);

    List<ReportType> getAllType();

    ReportType getTypeByName(String typeName);
}
