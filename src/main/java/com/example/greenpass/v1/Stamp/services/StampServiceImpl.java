package com.example.greenpass.v1.Stamp.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.repositories.ParkRepository;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.services.ParkRangerService;
import com.example.greenpass.v1.Stamp.entities.Stamp;
import com.example.greenpass.v1.Stamp.dtos.StampResponse;
import com.example.greenpass.v1.Stamp.dtos.VisitStatisticsResponse;
import com.example.greenpass.v1.Stamp.dtos.VisitStatisticsResponse.HistoryItem;
import com.example.greenpass.v1.Stamp.dtos.VisitStatisticsResponse.PeriodStatistics;
import com.example.greenpass.v1.Stamp.repositories.StampRepository;
import com.example.greenpass.v1.User.entities.User;
import com.example.greenpass.v1.User.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampServiceImpl implements StampService {
    private final UserService userService;
    private final ParkRangerService parkRangerService;
    private final ParkRepository parkRepository;
    private final StampRepository stampRepository;

    @Override
    public List<StampResponse> getAllStampsByUsername(String username) {
        List<StampResponse> responses = new ArrayList<>();
        stampRepository.findAllByUserUsername(username).stream()
                .filter(stamp -> stamp != null && stamp.getPark() != null && stamp.getParkRanger() != null)
                .forEach(stamp -> responses.add(StampResponse.builder()
                        .stampId(stamp.getStampId())
                        .stampDate(stamp.getStampDate())
                        .time(stamp.getTime())
                        .parkId(stamp.getPark().getParkId())
                        .parkName(stamp.getPark().getName())
                        .parkRangerName(
                                stamp.getParkRanger().getFirstname() + " " + stamp.getParkRanger().getSurname())
                        .signature(stamp.getParkRanger().getSignature())
                        .build()));
        return responses;
    }

    @Override
    public Stamp getStampById(int id) {
        return stampRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Stamp> getAllStampsByUsernameAndParkId(String username, int parkId) {
        return stampRepository.findAllByUserUsernameAndParkParkId(username, parkId);
    }

    @Override
    public void stampUser(String username, String parkrangerUsername) {
        User user = userService.getUserByUsername(username);
        ParkRanger parkRanger = parkRangerService.getParkRangerByUsername(parkrangerUsername);
        Park park = parkRanger.getPark();

        // if (park != null && hasUserBeenStampedWithinHours(username, park.getParkId(),
        // 2)) {
        // throw new IllegalStateException("นักท่องเที่ยวรายนี้ได้รับสแตมป์ของ " +
        // park.getName()
        // + " ไปแล้ว ไม่สามารถสแกนซ้ำได้ภายใน 2 ชั่วโมง");
        // }

        Stamp newStamp = Stamp.builder()
                .stampDate(LocalDate.now())
                .time(LocalTime.now())
                .user(user)
                .park(park)
                .parkRanger(parkRanger)
                .build();

        stampRepository.save(newStamp);
    }

    @Override
    public boolean hasUserBeenStampedToday(String username, Integer parkId) {
        if (username == null || parkId == null)
            return false;
        return stampRepository.existsByUserUsernameAndParkParkIdAndStampDate(username, parkId, LocalDate.now());
    }

    @Override
    public boolean hasUserBeenStampedWithinHours(String username, Integer parkId, int hours) {
        if (username == null || parkId == null) {
            return false;
        }
        Optional<Stamp> latestStampOpt = stampRepository
                .findTopByUserUsernameAndParkParkIdOrderByStampDateDescTimeDesc(username, parkId);
        if (latestStampOpt.isEmpty()) {
            return false;
        }
        Stamp latestStamp = latestStampOpt.get();
        if (latestStamp.getStampDate() == null || latestStamp.getTime() == null) {
            return false;
        }
        LocalDateTime lastStampDateTime = LocalDateTime.of(latestStamp.getStampDate(), latestStamp.getTime());
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(lastStampDateTime.plusHours(hours));
    }

    @Override
    public VisitStatisticsResponse getVisitStatistics() {
        return getVisitStatistics(null, null);
    }

    @Override
    public VisitStatisticsResponse getVisitStatistics(Integer parkId, String username) {
        if (parkId == null && username != null && !username.isBlank()) {
            ParkRanger ranger = parkRangerService.getParkRangerByUsername(username.trim());
            if (ranger != null && ranger.getPark() != null) {
                parkId = ranger.getPark().getParkId();
            }
        }

        int currentYear = LocalDate.now().getYear();
        int startYear = 2023;
        int endYear = Math.max(currentYear, 2026);

        Map<Integer, Map<Integer, long[]>> yearMonthCounts = new LinkedHashMap<>();
        Map<Integer, long[]> yearly = new LinkedHashMap<>();

        for (int y = startYear; y <= endYear; y++) {
            yearMonthCounts.put(y, new LinkedHashMap<>());
            yearly.put(y, new long[2]);
        }

        List<Object[]> rows = (parkId != null)
                ? stampRepository.findVisitStatisticsByParkId(parkId)
                : stampRepository.findVisitStatistics();

        for (Object[] row : rows) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            boolean foreigner = (Boolean) row[2];
            long count = ((Number) row[3]).longValue();

            yearly.computeIfAbsent(year, ignored -> new long[2])[foreigner ? 1 : 0] += count;
            yearMonthCounts.computeIfAbsent(year, ignored -> new LinkedHashMap<>())
                    .computeIfAbsent(month, ignored -> new long[2])[foreigner ? 1 : 0] += count;
        }

        Map<String, PeriodStatistics> monthlyStatsByYear = new LinkedHashMap<>();
        for (int y = startYear; y <= endYear; y++) {
            Map<Integer, long[]> months = yearMonthCounts.getOrDefault(y, java.util.Collections.emptyMap());
            List<HistoryItem> history = new ArrayList<>(12);
            for (int m = 1; m <= 12; m++) {
                long[] val = months.getOrDefault(m, new long[2]);
                history.add(HistoryItem.builder()
                        .label(monthLabel(m))
                        .thai(val[0])
                        .foreigner(val[1])
                        .build());
            }
            monthlyStatsByYear.put("ปี " + y, periodStatistics(history));
        }

        List<HistoryItem> yearlyHistory = new ArrayList<>();
        for (int y = startYear; y <= endYear; y++) {
            long[] val = yearly.getOrDefault(y, new long[2]);
            yearlyHistory.add(HistoryItem.builder()
                    .label("ปี " + y)
                    .thai(val[0])
                    .foreigner(val[1])
                    .build());
        }
        PeriodStatistics yearlyStats = periodStatistics(yearlyHistory);

        PeriodStatistics currentMonthlyStats = monthlyStatsByYear.getOrDefault("ปี " + currentYear, periodStatistics(new ArrayList<>()));

        long totalThai = yearly.values().stream().mapToLong(v -> v[0]).sum();
        long totalForeigner = yearly.values().stream().mapToLong(v -> v[1]).sum();

        String parkName = null;
        if (parkId != null) {
            Park park = parkRepository.findById(parkId).orElse(null);
            if (park != null) {
                parkName = park.getName();
            }
        }

        return VisitStatisticsResponse.builder()
                .thai(totalThai)
                .foreigner(totalForeigner)
                .total(totalThai + totalForeigner)
                .monthlyStats(currentMonthlyStats)
                .yearlyStats(yearlyStats)
                .monthlyStatsByYear(monthlyStatsByYear)
                .parkId(parkId)
                .parkName(parkName)
                .build();
    }

    private PeriodStatistics periodStatistics(List<HistoryItem> history) {
        long thai = history.stream().mapToLong(HistoryItem::getThai).sum();
        long foreigner = history.stream().mapToLong(HistoryItem::getForeigner).sum();
        return PeriodStatistics.builder()
                .thai(thai)
                .foreigner(foreigner)
                .total(thai + foreigner)
                .history(history)
                .build();
    }

    private String monthLabel(int month) {
        return switch (Month.of(month)) {
            case JANUARY -> "ม.ค.";
            case FEBRUARY -> "ก.พ.";
            case MARCH -> "มี.ค.";
            case APRIL -> "เม.ย.";
            case MAY -> "พ.ค.";
            case JUNE -> "มิ.ย.";
            case JULY -> "ก.ค.";
            case AUGUST -> "ส.ค.";
            case SEPTEMBER -> "ก.ย.";
            case OCTOBER -> "ต.ค.";
            case NOVEMBER -> "พ.ย.";
            case DECEMBER -> "ธ.ค.";
        };
    }

}
