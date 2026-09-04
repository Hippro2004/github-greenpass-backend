package com.example.greenpass.v1.Report.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Notification.services.NotificationService;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.services.ParkService;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.repositories.ParkRangerRepository;
import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.ReplyReport.services.ReplyReportService;
import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.Report.repositories.ReportRepository;
import com.example.greenpass.v1.ReportType.entities.ReportType;
import com.example.greenpass.v1.ReportType.services.ReporyTypeService;
import com.example.greenpass.v1.User.entities.User;
import com.example.greenpass.v1.User.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReplyReportService replyReportService;
    private final UserService userService;
    private final ParkService parkService;
    private final ParkRangerRepository parkRangerRepository;
    private final ReporyTypeService reportTypeService;
    private final NotificationService notificationService;

    private ReportResponse mapToResponse(Report r) {
        String rangerName = "-";
        if (r.getPark() != null && r.getPark().getParkRangers() != null && !r.getPark().getParkRangers().isEmpty()) {
            ParkRanger ranger = r.getPark().getParkRangers().get(0);
            rangerName = ranger.getFirstname() + " " + ranger.getSurname();
        }
        return ReportResponse.builder()
                .reportId(r.getReportId())
                .name(r.getName())
                .description(r.getDescription())
                .status(r.getStatus())
                .reportDate(r.getReportDate())
                .reportTime(r.getReportTime())
                .parkId(r.getPark() != null ? r.getPark().getParkId() : 0)
                .parkName(r.getPark() != null ? r.getPark().getName() : "")
                .username(r.getUser() != null ? r.getUser().getUsername() : "")
                .image(r.getImage())
                .parkRangerName(rangerName)
                .build();
    }

    @Override
    public List<ReportResponse> getAllByUsername(String username) {
        return reportRepository.findAllByUserUsername(username).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Report getByUsername(String name) {
        return reportRepository.findByUserUsername(name).orElse(null);
    }

    @Override
    public void addReport(AddReportDto addReportDto, String username) {
        User user = userService.getUserByUsername(username);
        Park park = parkService.getParkById(addReportDto.getParkId());
        ReportType type = reportTypeService.getTypeByName(addReportDto.getTypeName());

        if (user != null) {
            Report addReport = Report.builder()
                    .name(addReportDto.getName())
                    .description(addReportDto.getDescription())
                    .reportDate(LocalDate.now())
                    .reportTime(LocalTime.now())
                    .status("Pending")
                    .image(addReportDto.getImage())
                    .park(park)
                    .user(user)
                    .type(type)
                    .build();
            reportRepository.save(addReport);

            ReplyReport replyReport = ReplyReport.builder()
                    .updateDate(addReport.getReportDate())
                    .updateTime(addReport.getReportTime())
                    .progress(null)
                    .currentStatus(addReport.getStatus())
                    .image(addReport.getImage())
                    .report(addReport)
                    .parkRanger(null)
                    .build();
            replyReportService.addReplyReport(replyReport, addReport);

            notificationService.sendParkNotification(park,
                    "มีรายงานปัญหาใหม่ (" + type.getTypeName() + ")",
                    addReport.getName() + ": " + addReport.getDescription(),
                    addReport.getReportId());

        }

    }

    @Override
    public Report getByReportId(int id) {
        return reportRepository.findByReportId(id).orElse(null);
    }

    @Override
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllByOrderByReportIdDesc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReportResponse> getReportsByParkId(int parkId) {
        return reportRepository.findAllByParkParkIdOrderByReportIdDesc(parkId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReportResponse> getReportsByRangerUsername(String rangerUsername) {
        ParkRanger ranger = parkRangerRepository.findByUsername(rangerUsername);
        if (ranger != null && ranger.getPark() != null) {
            return getReportsByParkId(ranger.getPark().getParkId());
        }
        return List.of();
    }

    @Override
    public ReportResponse updateReportStatus(int reportId, String status, String rangerUsername) {
        Report report = reportRepository.findByReportId(reportId).orElse(null);
        if (report != null) {
            report.setStatus(status);
            reportRepository.save(report);

            ParkRanger ranger = null;
            if (rangerUsername != null && !rangerUsername.isEmpty()) {
                ranger = parkRangerRepository.findByUsername(rangerUsername);
            }

            ReplyReport replyReport = ReplyReport.builder()
                    .updateDate(LocalDate.now())
                    .progress("Status updated to " + status)
                    .currentStatus(status)
                    .image(report.getImage())
                    .report(report)
                    .parkRanger(ranger)
                    .build();
            replyReportService.addReplyReport(replyReport, report);

            return mapToResponse(report);
        }
        return null;
    }

}
