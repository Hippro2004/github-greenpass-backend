package com.example.greenpass.v1.ReplyReport.services;

import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.Report.entities.Report;

public interface ReplyReportService {
    void addReplyReport(ReplyReport replyReport, Report Report);
}
