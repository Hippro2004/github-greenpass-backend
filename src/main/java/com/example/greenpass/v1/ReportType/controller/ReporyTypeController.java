package com.example.greenpass.v1.ReportType.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.ReportType.dtos.ReportTypeResponse;
import com.example.greenpass.v1.ReportType.entities.ReportType;
import com.example.greenpass.v1.ReportType.services.ReporyTypeService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/report-type")
@RequiredArgsConstructor
public class ReporyTypeController {

    private final ReporyTypeService reporyTypeService;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllReportType() {
        try {
            List<ReportType> reportTypes = reporyTypeService.getAllType();
            if (reportTypes.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(false, "Not Found", null), HttpStatus.NOT_FOUND);
            } else {
                List<ReportTypeResponse> reportTypeResponses = reportTypes.stream()
                        .map(reportType -> new ReportTypeResponse(reportType.getTypeName()))
                        .collect(Collectors.toList());

                return new ResponseEntity<>(new ResponseObject(true, "OK", reportTypeResponses), HttpStatus.OK);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
