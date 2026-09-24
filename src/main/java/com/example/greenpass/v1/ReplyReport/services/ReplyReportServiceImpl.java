package com.example.greenpass.v1.ReplyReport.services;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.greenpass.v1.ReplyReport.dtos.ReplyReportResponse;
import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.ReplyReport.repositories.ReplyReporyRepository;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.utils.FileUtils;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Builder
public class ReplyReportServiceImpl implements ReplyReportService {

    private final ReplyReporyRepository replyReporyRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ReplyReport addReplyReport(ReplyReport replyReport, Report report) {
        ReplyReport addReplyReport = ReplyReport.builder()
                .updateDate(replyReport.getUpdateDate())
                .updateTime(replyReport.getUpdateTime())
                .progress(replyReport.getProgress())
                .currentStatus(replyReport.getCurrentStatus())
                .image(FileUtils.extractFileName(replyReport.getImage(), "reports"))
                .report(report)
                .parkRanger(replyReport.getParkRanger())
                .build();
        ReplyReport saved = replyReporyRepository.save(addReplyReport);

        ReplyReportResponse responseDto = mapToResponse(saved);

        // ส่งข้อความแจ้งเตือนผ่าน WebSocket ไปยัง reply-report topics
        try {
            // แจ้งเตือนไปยังฝั่ง Park (เจ้าหน้าที่อุทยาน)
            if (report != null && report.getPark() != null) {
                int parkId = report.getPark().getParkId();
                messagingTemplate.convertAndSend("/topic/park/" + parkId + "/reply-reports", responseDto);
            }

            // แจ้งเตือนไปยังผู้ใช้เจ้าของรายงาน (User)
            if (report != null && report.getUser() != null && report.getUser().getUsername() != null) {
                String uname = report.getUser().getUsername();
                messagingTemplate.convertAndSend("/topic/user/" + uname + "/reply-reports", responseDto);
                messagingTemplate.convertAndSend("/topic/user/" + uname.toLowerCase() + "/reply-reports", responseDto);
            }

            // ส่งไปยัง topic กลางของ reply-reports
            messagingTemplate.convertAndSend("/topic/reply-reports", responseDto);
        } catch (Exception e) {
            System.err.println("Could not send WebSocket reply-report notification: " + e.getMessage());
        }

        return saved;
    }

    private ReplyReportResponse mapToResponse(ReplyReport e) {
        String rangerFullName = null;
        String rangerUsername = null;
        if (e.getParkRanger() != null) {
            rangerFullName = (e.getParkRanger().getFirstname() + " " + e.getParkRanger().getSurname()).trim();
            rangerUsername = e.getParkRanger().getUsername();
        }
        return ReplyReportResponse.builder()
                .replyReportId(e.getReplyReportId())
                .reportId(e.getReport() != null ? e.getReport().getReportId() : null)
                .updateDate(e.getUpdateDate())
                .updateTime(e.getUpdateTime())
                .progress(e.getProgress())
                .currentStatus(e.getCurrentStatus())
                .image(FileUtils.extractFileName(e.getImage(), "reports"))
                .parkRangerName(rangerFullName)
                .parkRangerUsername(rangerUsername)
                .build();
    }

    @Override
    public List<ReplyReportResponse> getReplyReportByReportId(int reportId) {
        return replyReporyRepository.findAllByReportReportId(reportId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReplyReportResponse> getReplyReportByParkId(int parkId) {
        return replyReporyRepository.findAllByReportParkParkIdOrderByReplyReportIdDesc(parkId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReplyReportResponse> getReplyReportByUsername(String username) {
        return replyReporyRepository.findAllByReportUserUsernameOrderByReplyReportIdDesc(username).stream()
                .map(this::mapToResponse)
                .toList();
    }

}
