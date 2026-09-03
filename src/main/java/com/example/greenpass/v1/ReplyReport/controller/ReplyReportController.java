package com.example.greenpass.v1.ReplyReport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.ReplyReport.dtos.ReplyReportResponse;
import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.ReplyReport.services.ReplyReportService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/reply-report")
@RequiredArgsConstructor
public class ReplyReportController {

    private final ReplyReportService replyReportService;

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

}
