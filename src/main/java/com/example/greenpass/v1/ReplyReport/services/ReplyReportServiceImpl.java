package com.example.greenpass.v1.ReplyReport.services;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.ReplyReport.repositories.ReplyReporyRepository;
import com.example.greenpass.v1.Report.entities.Report;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Builder
public class ReplyReportServiceImpl implements ReplyReportService {

    private final ReplyReporyRepository replyReporyRepository;

    @Override
    public void addReplyReport(ReplyReport replyReport, Report report) {
        ReplyReport addReplyReport = ReplyReport.builder()
                .updateDate(report.getReportDate()).progress(null)
                .currentStatus("Pending")
                .image(report.getImage())
                .report(report)
                .parkRanger(null)
                .build();
        replyReporyRepository.save(addReplyReport);
    }

}
