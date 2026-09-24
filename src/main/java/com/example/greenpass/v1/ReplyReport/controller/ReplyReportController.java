package com.example.greenpass.v1.ReplyReport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.ReplyReport.dtos.ReplyReportResponse;
import com.example.greenpass.v1.ReplyReport.services.ReplyReportService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.services.ReportService;

@RestController
@RequestMapping("/reply-report")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReplyReportController {

    private final ReplyReportService replyReportService;
    private final ReportService reportService;

    @PostMapping({"", "/add"})
    public ResponseEntity<ResponseObject> createReplyReport(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "username", required = false) String usernameHeader) {
        try {
            int reportId = 0;
            if (payload.containsKey("reportId") && payload.get("reportId") != null) {
                reportId = Integer.parseInt(payload.get("reportId").toString());
            } else if (payload.containsKey("report_id") && payload.get("report_id") != null) {
                reportId = Integer.parseInt(payload.get("report_id").toString());
            }

            if (reportId == 0) {
                return new ResponseEntity<>(new ResponseObject(false, "Report ID is required", null),
                        HttpStatus.BAD_REQUEST);
            }

            String status = payload.get("status") != null ? payload.get("status").toString() : null;
            if (status == null && payload.containsKey("currentStatus") && payload.get("currentStatus") != null) {
                status = payload.get("currentStatus").toString();
            }
            String progress = payload.get("progress") != null ? payload.get("progress").toString() : null;
            String image = payload.get("image") != null ? payload.get("image").toString() : null;
            String username = usernameHeader;
            if ((username == null || username.isBlank()) && payload.containsKey("username") && payload.get("username") != null) {
                username = payload.get("username").toString();
            }
            if ((username == null || username.isBlank()) && payload.containsKey("rangerUsername") && payload.get("rangerUsername") != null) {
                username = payload.get("rangerUsername").toString();
            }
            if ((username == null || username.isBlank()) && payload.containsKey("parkRangerUsername") && payload.get("parkRangerUsername") != null) {
                username = payload.get("parkRangerUsername").toString();
            }

            ReportResponse updated = reportService.updateReportStatus(reportId, status, progress, image, username);
            if (updated == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Report not found for update", null),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseObject(true, "Reply report created successfully", updated),
                    HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ResponseObject(false, e.getMessage(), null),
                    HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to create reply report: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-reply-report")
    public ResponseEntity<ResponseObject> getReplyReport(@RequestParam("reportId") int reportId) {
        try {
            List<ReplyReportResponse> replyReports = replyReportService.getReplyReportByReportId(reportId);
            if (replyReports.isEmpty() || replyReports == null) {
                return new ResponseEntity<>(new ResponseObject(false, "Reply reports not found", null),
                        HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ResponseObject(true, "Reply reports found", replyReports),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Failed to retrieve reply reports", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/park/{parkId}")
    public ResponseEntity<ResponseObject> getReplyReportsByPark(@PathVariable Integer parkId) {
        try {
            List<ReplyReportResponse> replyReports = replyReportService.getReplyReportByParkId(parkId);
            return new ResponseEntity<>(new ResponseObject(true, "Reply reports found successfully", replyReports),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-reply-reports")
    public ResponseEntity<ResponseObject> getReplyReportsUser(@RequestHeader("username") String username) {
        try {
            List<ReplyReportResponse> replyReports = replyReportService.getReplyReportByUsername(username);
            return new ResponseEntity<>(new ResponseObject(true, "Reply reports found successfully", replyReports),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(false, "Internal Server Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
