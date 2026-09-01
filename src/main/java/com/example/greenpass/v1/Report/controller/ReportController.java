package com.example.greenpass.v1.Report.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/my-reports")
    public ResponseEntity<ResponseObject> getAllByUsername(@RequestHeader("username") String username) {
        try {
            List<ReportResponse> reports = reportService.getAllByUsername(username);
            return new ResponseEntity<>(new ResponseObject(true, "Reports found", reports), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve reports", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllReports() {
        try {
            List<ReportResponse> reports = reportService.getAllReports();
            return new ResponseEntity<>(new ResponseObject(true, "All reports found", reports), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve reports", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/park/{parkId}")
    public ResponseEntity<ResponseObject> getReportsByParkId(@PathVariable int parkId) {
        try {
            List<ReportResponse> reports = reportService.getReportsByParkId(parkId);
            return new ResponseEntity<>(new ResponseObject(true, "Reports for park found", reports), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve park reports", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ranger/{username}")
    public ResponseEntity<ResponseObject> getReportsByRangerUsername(@PathVariable String username) {
        try {
            List<ReportResponse> reports = reportService.getReportsByRangerUsername(username);
            return new ResponseEntity<>(new ResponseObject(true, "Reports for ranger found", reports), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve ranger reports", null),
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
    public ResponseEntity<ResponseObject> getReportById(@PathVariable int id) {
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

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseObject> updateReportStatus(@PathVariable int id,
            @RequestBody Map<String, String> payload,
            @RequestHeader(value = "username", required = false) String username) {
        try {
            String status = payload.get("status");
            ReportResponse updated = reportService.updateReportStatus(id, status, username);
            if (updated == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Report not found for update", null),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseObject(true, "Status updated successfully", updated),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to update report status", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

