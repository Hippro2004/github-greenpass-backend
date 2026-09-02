package com.example.greenpass.v1.ReportType.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.greenpass.v1.ReportType.entities.ReportType;
import com.example.greenpass.v1.ReportType.repositories.ReportTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportTypeDataLoader implements CommandLineRunner {

    private final ReportTypeRepository reportTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        // รายการประเภทรายงานที่ต้องการ auto-insert เมื่อเริ่มรันระบบ
        List<String> defaultTypes = List.of("ปกติ", "ร้ายแรง");

        for (String typeName : defaultTypes) {
            if (!reportTypeRepository.existsByTypeName(typeName)) {
                ReportType reportType = ReportType.builder()
                        .typeName(typeName)
                        .build();
                reportTypeRepository.save(reportType);
                log.info("Auto-inserted ReportType: {}", typeName);
            }
        }
    }
}
