package com.example.greenpass.v1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.dtos.AddReportDto;
import com.example.greenpass.v1.entities.Report;
import com.example.greenpass.v1.services.ReportService;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Builder
public class ReportController {
    private final ReportService reportService;
    // private final UserService userService;

    @GetMapping("/my-reports")
    public ResponseEntity<ResponseObject> getAllByUsername(@RequestHeader("username") String username) {
        try {
            List<Report> reports = reportService.getAllByUsername(username);
            return new ResponseEntity<>(new ResponseObject(true, "Reports found", reports), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve reports", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-report")
    public ResponseEntity<ResponseObject> addReport(@RequestHeader("username") String username,
            @RequestBody @Valid AddReportDto addReportDto) {
        try {
            reportService.addReport(addReportDto);
            return new ResponseEntity<>(new ResponseObject(true, "Add Report Success", null),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to add report", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
