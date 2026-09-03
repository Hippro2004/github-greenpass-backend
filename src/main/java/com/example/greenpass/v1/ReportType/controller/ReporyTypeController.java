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
            List<ReportTypeResponse> reportTypes = reporyTypeService.getAllType();
            if (reportTypes.isEmpty() || reportTypes == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Not Found", null), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(new ResponseObject(true, "OK", reportTypes), HttpStatus.OK);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
