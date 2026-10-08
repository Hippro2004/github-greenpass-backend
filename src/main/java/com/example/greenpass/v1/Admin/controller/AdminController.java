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
import com.example.greenpass.v1.Announcement.repositories.AnnouncementRepository;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.repositories.ParkRepository;
import com.example.greenpass.v1.ParkRanger.repositories.ParkRangerRepository;
import com.example.greenpass.v1.Report.repositories.ReportRepository;
import com.example.greenpass.v1.Stamp.repositories.StampRepository;

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
    private final StampRepository stampRepository;

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

            Integer targetYear = (year != null && year > 0) ? (year > 2500 ? year - 543 : year) : null;
            Integer targetMonth = (month != null && month >= 1 && month <= 12) ? month : null;

            // 1. Fetch aggregated stats directly from Database using SQL/JPQL GROUP BY
            // (Blazing Fast!)
            List<Object[]> newsRows = announcementRepository.countAnnouncementsByParkAndFilters(targetYear,
                    targetMonth);
            List<Object[]> reportRows = reportRepository.countReportsByParkAndFilters(targetYear, targetMonth);
            List<Object[]> visitorRows = stampRepository.countVisitorsByParkAndFilters(targetYear, targetMonth);

            Map<Integer, Long> newsByPark = new HashMap<>();
            for (Object[] r : newsRows) {
                if (r[0] != null) {
                    newsByPark.put((Integer) r[0], ((Number) r[1]).longValue());
                }
            }

            Map<Integer, Long> inProgressReportsByPark = new HashMap<>();
            Map<Integer, Long> completedReportsByPark = new HashMap<>();
            Map<Integer, Long> totalReportsByPark = new HashMap<>();
            long totalReportCount = 0;
            long totalProcessingReportCount = 0;
            long totalCompletedReportCount = 0;

            for (Object[] r : reportRows) {
                if (r[0] != null) {
                    Integer parkId = (Integer) r[0];
                    String status = (String) r[1];
                    long count = ((Number) r[2]).longValue();

                    totalReportsByPark.merge(parkId, count, Long::sum);
                    totalReportCount += count;

                    if (status != null) {
                        if ("Pending".equalsIgnoreCase(status) || "Acknowledged".equalsIgnoreCase(status)
                                || "รับทราบ".equalsIgnoreCase(status) || "InProgress".equalsIgnoreCase(status)
                                || "แจ้งรายงาน".equalsIgnoreCase(status) || "กำลังดำเนินการ".equalsIgnoreCase(status)) {
                            inProgressReportsByPark.merge(parkId, count, Long::sum);
                            totalProcessingReportCount += count;
                        } else if ("Completed".equalsIgnoreCase(status)
                                || "ดำเนินการแก้ไขสำเร็จ".equalsIgnoreCase(status)
                                || "ดำเนินการสำเร็จ".equalsIgnoreCase(status)) {
                            completedReportsByPark.merge(parkId, count, Long::sum);
                            totalCompletedReportCount += count;
                        }
                    }
                }
            }

            Map<Integer, Long> thaiVisitorsByPark = new HashMap<>();
            Map<Integer, Long> foreignVisitorsByPark = new HashMap<>();

            for (Object[] r : visitorRows) {
                if (r[0] != null) {
                    Integer parkId = (Integer) r[0];
                    Boolean isForeigner = (Boolean) r[1];
                    long count = ((Number) r[2]).longValue();

                    if (Boolean.TRUE.equals(isForeigner)) {
                        foreignVisitorsByPark.merge(parkId, count, Long::sum);
                    } else {
                        thaiVisitorsByPark.merge(parkId, count, Long::sum);
                    }
                }
            }

            long totalNewsCount = newsByPark.values().stream().mapToLong(Long::longValue).sum();
            List<Park> parks = parkRepository.findAll();

            List<StatisticsResponse.ParkStatDto> parkStats = parks.stream().map(p -> {
                int parkId = p.getParkId();
                long newsCount = newsByPark.getOrDefault(parkId, 0L);
                long totReports = totalReportsByPark.getOrDefault(parkId, 0L);
                long inProg = inProgressReportsByPark.getOrDefault(parkId, 0L);
                long comp = completedReportsByPark.getOrDefault(parkId, 0L);
                long thaiVisitors = thaiVisitorsByPark.getOrDefault(parkId, 0L);
                long foreignVisitors = foreignVisitorsByPark.getOrDefault(parkId, 0L);
                long totalVisitors = thaiVisitors + foreignVisitors;

                String province = "ทั่วไป";
                if (p.getAddress() != null) {
                    String addr = p.getAddress();
                    if (addr.contains("จ.")) {
                        String sub = addr.substring(addr.indexOf("จ.") + 2).trim();
                        province = sub.split("[\\s,]+")[0];
                    } else if (addr.contains("จังหวัด")) {
                        String sub = addr.substring(addr.indexOf("จังหวัด") + 7).trim();
                        province = sub.split("[\\s,]+")[0];
                    }
                }

                if ("ทั่วไป".equals(province) || province.isBlank()) {
                    String name = p.getName() != null ? p.getName() : "";
                    if (name.contains("เขาใหญ่"))
                        province = "นครราชสีมา";
                    else if (name.contains("แก่งกระจาน"))
                        province = "เพชรบุรี";
                    else if (name.contains("เอราวัณ"))
                        province = "กาญจนบุรี";
                    else if (name.contains("ดอยสุเทพ") || name.contains("ดอยอินทนนท์"))
                        province = "เชียงใหม่";
                }

                return StatisticsResponse.ParkStatDto.builder()
                        .parkId(parkId)
                        .parkName(p.getName())
                        .province(province)
                        .announcements(newsCount)
                        .totalReports((int) totReports)
                        .inProgress(inProg)
                        .completed(comp)
                        .thaiVisitors(thaiVisitors)
                        .foreignVisitors(foreignVisitors)
                        .totalVisitors(totalVisitors)
                        .build();
            }).toList();

            long sumTotalVisitors = parkStats.stream().mapToLong(StatisticsResponse.ParkStatDto::getTotalVisitors)
                    .sum();
            long sumThaiVisitors = parkStats.stream().mapToLong(StatisticsResponse.ParkStatDto::getThaiVisitors).sum();
            long sumForeignVisitors = parkStats.stream().mapToLong(StatisticsResponse.ParkStatDto::getForeignVisitors)
                    .sum();

            Map<String, Object> metrics = new HashMap<>();
            metrics.put("totalPark", totalPark);
            metrics.put("totalRanger", totalRanger);
            metrics.put("totalNews", totalNewsCount);
            metrics.put("totalReport", totalReportCount);
            metrics.put("totalProcessingReport", totalProcessingReportCount);
            metrics.put("totalCompletedReport", totalCompletedReportCount);
            metrics.put("totalVisitors", sumTotalVisitors);
            metrics.put("totalThaiVisitors", sumThaiVisitors);
            metrics.put("totalForeignVisitors", sumForeignVisitors);

            StatisticsResponse responseData = StatisticsResponse.builder()
                    .metrics(metrics)
                    .parkStats(parkStats)
                    .build();

            return new ResponseEntity<>(new ResponseObject(true, "Statistics fetched successfully", responseData),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseObject(false, "Failed to fetch statistics", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
