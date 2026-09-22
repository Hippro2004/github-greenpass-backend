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
import com.example.greenpass.v1.ReplyReport.repositories.ReplyReporyRepository;
import com.example.greenpass.v1.ReplyReport.services.ReplyReportService;
import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.Report.repositories.ReportRepository;
import com.example.greenpass.v1.ReportType.entities.ReportType;
import com.example.greenpass.v1.ReportType.services.ReporyTypeService;
import com.example.greenpass.v1.ReportType.repositories.ReportTypeRepository;
import com.example.greenpass.v1.User.entities.User;
import com.example.greenpass.v1.User.services.UserService;
import com.example.greenpass.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReplyReportService replyReportService;
    private final ReplyReporyRepository replyReporyRepository;
    private final UserService userService;
    private final ParkService parkService;
    private final ParkRangerRepository parkRangerRepository;
    private final ReporyTypeService reportTypeService;
    private final ReportTypeRepository reportTypeRepository;
    private final NotificationService notificationService;

    private ReportResponse mapToResponse(Report r) {
        String rangerName = "ยังไม่มีผู้รับผิดชอบ";
        String rangerUsername = null;

        ParkRanger assignedRanger = r.getParkRanger();
        if (assignedRanger == null && r.getReportId() != null) {
            List<ReplyReport> replies = replyReporyRepository.findAllByReportReportId(r.getReportId());
            for (ReplyReport rep : replies) {
                if (rep.getParkRanger() != null) {
                    assignedRanger = rep.getParkRanger();
                    r.setParkRanger(assignedRanger);
                    reportRepository.save(r);
                    break;
                }
            }
        }

        if (assignedRanger != null) {
            rangerName = (assignedRanger.getFirstname() + " " + assignedRanger.getSurname()).trim();
            rangerUsername = assignedRanger.getUsername();
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
                .image(FileUtils.extractFileName(r.getImage(), "reports"))
                .parkRangerName(rangerName)
                .parkRangerUsername(rangerUsername)
                .typeName(r.getType() != null ? r.getType().getTypeName() : "ปกติ")
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
        
        String inputTypeName = addReportDto.getTypeName();
        ReportType type = null;
        if (inputTypeName != null && !inputTypeName.isBlank()) {
            String trimmed = inputTypeName.trim();
            type = reportTypeService.getTypeByName(trimmed);
            if (type == null) {
                if ("2".equals(trimmed) || trimmed.contains("ร้ายแรง") || trimmed.contains("ฉุกเฉิน") || trimmed.equalsIgnoreCase("severe") || trimmed.equalsIgnoreCase("emergency")) {
                    type = reportTypeRepository.findByTypeName("ร้ายแรง").orElse(null);
                } else if ("1".equals(trimmed) || trimmed.contains("ปกติ") || trimmed.equalsIgnoreCase("normal")) {
                    type = reportTypeRepository.findByTypeName("ปกติ").orElse(null);
                }
            }
        }

        if (type == null) {
            String combinedText = ((addReportDto.getName() != null ? addReportDto.getName() : "") + " " + (addReportDto.getDescription() != null ? addReportDto.getDescription() : "")).toLowerCase();
            if (combinedText.contains("ร้ายแรง") || combinedText.contains("ฉุกเฉิน") || combinedText.contains("emergency") || combinedText.contains("danger") || combinedText.contains("help")) {
                type = reportTypeRepository.findByTypeName("ร้ายแรง").orElse(null);
            } else {
                type = reportTypeRepository.findByTypeName("ปกติ").orElse(null);
            }
        }

        if (user != null) {
            String cleanImage = FileUtils.extractFileName(addReportDto.getImage(), "reports");
            Report addReport = Report.builder()
                    .name(addReportDto.getName())
                    .description(addReportDto.getDescription())
                    .reportDate(LocalDate.now())
                    .reportTime(LocalTime.now())
                    .status("Pending")
                    .image(cleanImage)
                    .park(park)
                    .user(user)
                    .type(type)
                    .build();
            Report saved = reportRepository.save(addReport);

            ReplyReport replyReport = ReplyReport.builder()
                    .updateDate(saved.getReportDate())
                    .updateTime(saved.getReportTime())
                    .progress(null)
                    .currentStatus(addReport.getStatus())
                    .image(cleanImage)
                    .report(saved)
                    .parkRanger(null)
                    .build();
            replyReportService.addReplyReport(replyReport, saved);

            String typeNameStr = (type != null && type.getTypeName() != null) 
                    ? type.getTypeName() 
                    : "ปกติ";

            boolean isSevere = (type != null && type.getTypeId() != null && type.getTypeId() == 2) 
                    || "ร้ายแรง".equals(typeNameStr);

            String titlePrefix = isSevere ? "🚨 แจ้งเตือนเหตุฉุกเฉินด่วน (ร้ายแรง)" : "มีรายงานปัญหาใหม่ (ปกติ)";

            notificationService.sendParkNotification(park,
                    titlePrefix,
                    addReport.getName() + ": " + addReport.getDescription(),
                    saved);

        }

    }

    @Override
    public Report getByReportId(int id) {
        Report report = reportRepository.findByReportId(id).orElse(null);
        if (report != null) {
            report.setImage(FileUtils.extractFileName(report.getImage(), "reports"));
            if (report.getParkRanger() == null) {
                List<ReplyReport> replies = replyReporyRepository.findAllByReportReportId(report.getReportId());
                for (ReplyReport rep : replies) {
                    if (rep.getParkRanger() != null) {
                        report.setParkRanger(rep.getParkRanger());
                        reportRepository.save(report);
                        break;
                    }
                }
            }
        }
        return report;
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
        return updateReportStatus(reportId, status, null, null, rangerUsername);
    }

    @Override
    public ReportResponse updateReportStatus(int reportId, String status, String progress, String image, String rangerUsername) {
        Report report = reportRepository.findByReportId(reportId).orElse(null);
        if (report == null) {
            return null;
        }

        // Backfill assigned ranger from existing replies if needed
        if (report.getParkRanger() == null) {
            List<ReplyReport> replies = replyReporyRepository.findAllByReportReportId(report.getReportId());
            for (ReplyReport rep : replies) {
                if (rep.getParkRanger() != null) {
                    report.setParkRanger(rep.getParkRanger());
                    reportRepository.save(report);
                    break;
                }
            }
        }

        ParkRanger currentRanger = null;
        if (rangerUsername != null && !rangerUsername.isBlank()) {
            currentRanger = parkRangerRepository.findByUsername(rangerUsername.trim());
        }

        // 🔒 ตรวจสอบสิทธิ์ผู้รับผิดชอบ:
        // หากรายงานนี้มีเจ้าหน้าที่ผู้รับผิดชอบอยู่แล้ว เจ้าหน้าที่คนอื่นไม่สามารถเข้ามาแก้ไขหรือดำเนินการแทนได้
        if (report.getParkRanger() != null) {
            String assignedUsername = report.getParkRanger().getUsername();
            if (rangerUsername != null && !rangerUsername.isBlank() && !assignedUsername.equalsIgnoreCase(rangerUsername.trim())) {
                String assignedFullName = (report.getParkRanger().getFirstname() + " " + report.getParkRanger().getSurname()).trim();
                throw new IllegalStateException("รายงานนี้อยู่ภายใต้ความรับผิดชอบของเจ้าหน้าที่ " + assignedFullName + " แล้ว เจ้าหน้าที่ท่านอื่นไม่สามารถดำเนินการแทนได้");
            }
        } else {
            // หากยังไม่มีผู้รับผิดชอบ ให้บันทึกเจ้าหน้าที่ผู้นี้เป็นผู้รับผิดชอบรายงานทันที
            if (currentRanger != null) {
                report.setParkRanger(currentRanger);
            }
        }

        report.setStatus(status);
        reportRepository.save(report);

        String progressText = (progress != null && !progress.isBlank()) ? progress : ("Status updated to " + status);
        String progressImage = (image != null && !image.isBlank())
                ? FileUtils.extractFileName(image, "reports")
                : FileUtils.extractFileName(report.getImage(), "reports");

        ReplyReport replyReport = ReplyReport.builder()
                .updateDate(LocalDate.now())
                .updateTime(LocalTime.now())
                .progress(progressText)
                .currentStatus(status)
                .image(progressImage)
                .report(report)
                .parkRanger(currentRanger != null ? currentRanger : report.getParkRanger())
                .build();
        replyReportService.addReplyReport(replyReport, report);

        String thaiStatus = "Pending".equals(status) ? "แจ้งรายงาน" : "InProgress".equals(status) ? "กำลังดำเนินการ" : "Completed".equals(status) ? "ดำเนินการแก้ไขสำเร็จ" : status;

        User reportOwner = report.getUser();
        if (reportOwner != null) {
            notificationService.sendUserNotification(
                    reportOwner,
                    "อัปเดตสถานะรายงาน (" + thaiStatus + ")",
                    "รายงาน '" + report.getName() + "' ของคุณได้รับการเปลี่ยนสถานะเป็น " + thaiStatus,
                    report);
        }

        return mapToResponse(report);
    }

}
