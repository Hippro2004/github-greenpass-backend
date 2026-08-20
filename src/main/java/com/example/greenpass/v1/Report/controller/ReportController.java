package com.example.greenpass.v1.Report.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.Report.services.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    // private final UserService userService;

    @GetMapping("/my-reports")
    public ResponseEntity<ResponseObject> getAllByUsername(@RequestHeader("username") String username) {
        try {

            List<ReportResponse> reports = reportService.getAllByUsername(username);

            if (reports.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(false, "Reports not found", null), HttpStatus.NOT_FOUND);

            }

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
            reportService.addReport(addReportDto, username);
            return new ResponseEntity<>(new ResponseObject(true, "Add Report Success", null),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to add report", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getReportById(@PathVariable Long id) {
        try {
            Report report = reportService.getByReportId(id);
            if (report == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Report not found", null), HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(new ResponseObject(true, "Report found", report), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Report not found", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
