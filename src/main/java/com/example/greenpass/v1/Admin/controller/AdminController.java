package com.example.greenpass.v1.Admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Admin.dtos.LoginAdminDto;
import com.example.greenpass.v1.Admin.dtos.StatisticsResponse;
import com.example.greenpass.v1.Admin.entities.Admin;
import com.example.greenpass.v1.Admin.services.AdminService;
import com.example.greenpass.v1.Announcement.entities.Announcement;
import com.example.greenpass.v1.Announcement.repositories.AnnouncementRepository;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.repositories.ParkRepository;
import com.example.greenpass.v1.ParkRanger.repositories.ParkRangerRepository;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.v1.Report.repositories.ReportRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ParkRepository parkRepository;
    private final ParkRangerRepository parkRangerRepository;
    private final AnnouncementRepository announcementRepository;
    private final ReportRepository reportRepository;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody @Valid LoginAdminDto loginAdminDto) {
        try {
            Admin admin = adminService.getAdmin(loginAdminDto.getUsername());

            if (admin != null) {
                if (admin.getPassword().equalsIgnoreCase(loginAdminDto.getPassword())) {
                    return new ResponseEntity<>(new ResponseObject(true, "Admin Login Successfully", admin),
                            HttpStatus.OK);
                }

                return new ResponseEntity<>(new ResponseObject(false, "Password incorrect", null),
                        HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ResponseObject(false, "Admin not found", null),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to Login", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<ResponseObject> getStatistics(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {
        try {
            long totalPark = parkRepository.count();
            long totalRanger = parkRangerRepository.count();

            List<Report> allReports = reportRepository.findAll();
            List<Announcement> announcements = announcementRepository.findAll();

            if (year != null && year > 0) {
                int targetYear = year > 2500 ? year - 543 : year;
                allReports = allReports.stream()
                        .filter(r -> r.getReportDate() != null && r.getReportDate().getYear() == targetYear)
                        .toList();
                announcements = announcements.stream()
                        .filter(a -> a.getPostDate() != null && a.getPostDate().getYear() == targetYear)
                        .toList();
            }

            if (month != null && month >= 1 && month <= 12) {
                allReports = allReports.stream()
                        .filter(r -> r.getReportDate() != null && r.getReportDate().getMonthValue() == month)
                        .toList();
                announcements = announcements.stream()
                        .filter(a -> a.getPostDate() != null && a.getPostDate().getMonthValue() == month)
                        .toList();
            }

            long totalNews = announcements.size();
            long totalReport = allReports.size();
            long totalProcessingReport = allReports.stream()
                    .filter(r -> r.getStatus() != null && (
                        "Pending".equalsIgnoreCase(r.getStatus()) || 
                        "InProgress".equalsIgnoreCase(r.getStatus()) || 
                        "แจ้งรายงาน".equalsIgnoreCase(r.getStatus()) || 
                        "กำลังดำเนินการ".equalsIgnoreCase(r.getStatus())
                    ))
                    .count();
            long totalCompletedReport = allReports.stream()
                    .filter(r -> r.getStatus() != null && (
                        "Completed".equalsIgnoreCase(r.getStatus()) || 
                        "ดำเนินการแก้ไขสำเร็จ".equalsIgnoreCase(r.getStatus()) || 
                        "ดำเนินการสำเร็จ".equalsIgnoreCase(r.getStatus())
                    ))
                    .count();

            Map<String, Object> metrics = new HashMap<>();
            metrics.put("totalPark", totalPark);
            metrics.put("totalRanger", totalRanger);
            metrics.put("totalNews", totalNews);
            metrics.put("totalReport", totalReport);
            metrics.put("totalProcessingReport", totalProcessingReport);
            metrics.put("totalCompletedReport", totalCompletedReport);

            List<Park> parks = parkRepository.findAll();
            final List<Report> finalReports = allReports;
            final List<Announcement> finalAnnouncements = announcements;

            List<StatisticsResponse.ParkStatDto> parkStats = parks.stream().map(p -> {
                long newsCount = finalAnnouncements.stream()
                        .filter(a -> a.getPark() != null && a.getPark().getParkId().equals(p.getParkId()))
                        .count();

                List<Report> pReports = finalReports.stream()
                        .filter(r -> r.getPark() != null && r.getPark().getParkId().equals(p.getParkId()))
                        .toList();

                long inProg = pReports.stream()
                        .filter(r -> r.getStatus() != null && (
                            "Pending".equalsIgnoreCase(r.getStatus()) || 
                            "InProgress".equalsIgnoreCase(r.getStatus()) || 
                            "แจ้งรายงาน".equalsIgnoreCase(r.getStatus()) || 
                            "กำลังดำเนินการ".equalsIgnoreCase(r.getStatus())
                        ))
                        .count();

                long comp = pReports.stream()
                        .filter(r -> r.getStatus() != null && (
                            "Completed".equalsIgnoreCase(r.getStatus()) || 
                            "ดำเนินการแก้ไขสำเร็จ".equalsIgnoreCase(r.getStatus()) || 
                            "ดำเนินการสำเร็จ".equalsIgnoreCase(r.getStatus())
                        ))
                        .count();

                String province = "ทั่วไป";
                if (p.getAddress() != null && p.getAddress().contains("จ.")) {
                    String sub = p.getAddress().substring(p.getAddress().indexOf("จ.") + 2).trim();
                    province = sub.split(" ")[0];
                }

                return StatisticsResponse.ParkStatDto.builder()
                        .parkId(p.getParkId())
                        .parkName(p.getName())
                        .province(province)
                        .announcements(newsCount)
                        .totalReports(pReports.size())
                        .inProgress(inProg)
                        .completed(comp)
                        .build();
            }).toList();

            StatisticsResponse responseData = StatisticsResponse.builder()
                    .metrics(metrics)
                    .parkStats(parkStats)
                    .build();

            return new ResponseEntity<>(new ResponseObject(true, "Statistics fetched successfully", responseData), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to fetch statistics", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}