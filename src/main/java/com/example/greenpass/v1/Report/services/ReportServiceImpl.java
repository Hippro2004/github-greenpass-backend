package com.example.greenpass.v1.Report.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.services.ParkService;
import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.ReplyReport.services.ReplyReportService;
import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.dtos.ReportResponse;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.Report.repositories.ReportRepository;
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

    @Override
    public List<ReportResponse> getAllByUsername(String username) {
        return reportRepository.findAllByUserUsername(username).stream()
                .map(r -> new ReportResponse(r.getName(), r.getDescription(), r.getStatus(), r.getReportDate()))
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

        if (user != null) {
            Report addReport = Report.builder()
                    .name(addReportDto.getName())
                    .description(addReportDto.getDescription())
                    .reportDate(LocalDate.now())
                    .status("Pending")
                    .image(addReportDto.getImage())
                    .park(park)
                    .user(user)
                    .build();
            reportRepository.save(addReport);

            ReplyReport replyReport = ReplyReport.builder()
                    .updateDate(addReport.getReportDate())
                    .progress(null)
                    .currentStatus(addReport.getStatus())
                    .image(addReport.getImage())
                    .report(addReport)
                    .parkRanger(null)
                    .build();
            replyReportService.addReplyReport(replyReport, addReport);
        }

    }

    @Override
    public Report getByReportId(int id) {
        return reportRepository.findByReportId(id).orElse(null);
    }

}
