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
                .updateDate(replyReport.getUpdateDate())
                .updateTime(replyReport.getUpdateTime())
                .progress(replyReport.getProgress())
                .currentStatus(replyReport.getCurrentStatus())
                .image(replyReport.getImage())
                .report(report)
                .parkRanger(replyReport.getParkRanger())
                .build();
        replyReporyRepository.save(addReplyReport);
    }

}
