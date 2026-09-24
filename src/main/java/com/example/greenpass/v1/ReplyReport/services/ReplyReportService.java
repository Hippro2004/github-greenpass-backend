package com.example.greenpass.v1.ReplyReport.services;

import java.util.List;

import com.example.greenpass.v1.ReplyReport.dtos.ReplyReportResponse;
import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.Report.entities.Report;

public interface ReplyReportService {
    ReplyReport addReplyReport(ReplyReport replyReport, Report report);

    List<ReplyReportResponse> getReplyReportByReportId(int reportId);

    List<ReplyReportResponse> getReplyReportByParkId(int parkId);

    List<ReplyReportResponse> getReplyReportByUsername(String username);
}
