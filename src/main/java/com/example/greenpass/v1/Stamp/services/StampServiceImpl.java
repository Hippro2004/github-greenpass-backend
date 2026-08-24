package com.example.greenpass.v1.Stamp.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.ParkRanger.entities.ParkRanger;
import com.example.greenpass.v1.ParkRanger.services.ParkRangerService;
import com.example.greenpass.v1.Stamp.entities.Stamp;
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
    private final StampRepository stampRepository;

    @Override
    public List<Stamp> getAllStampsByUsername(String username) {
        return stampRepository.findAllByUserUsername(username);
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
    public void StampUser(String username, String parkrangerUsername) {
        User user = userService.getUserByUsername(username);
        ParkRanger parkRanger = parkRangerService.getParkRangerByUsername(parkrangerUsername);
        Park park = parkRanger.getPark();

        if (park != null && hasUserBeenStampedToday(username, park.getParkId())) {
            throw new IllegalStateException("นักท่องเที่ยวรายนี้ได้รับสแตมป์ของ" + park.getName()
                    + "ในวันนี้ไปแล้ว ไม่สามารถสแกนซ้ำได้ภายใน 1 วัน");
        }

        Stamp newStamp = Stamp.builder()
                .stampDate(LocalDate.now())
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
    public VisitStatisticsResponse getVisitStatistics() {
        int currentYear = LocalDate.now().getYear();
        Map<Integer, long[]> monthly = new LinkedHashMap<>();
        Map<Integer, long[]> yearly = new LinkedHashMap<>();

        for (Object[] row : stampRepository.findVisitStatistics()) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            boolean foreigner = (Boolean) row[2];
            long count = ((Number) row[3]).longValue();

            yearly.computeIfAbsent(year, ignored -> new long[2])[foreigner ? 1 : 0] += count;
            if (year == currentYear) {
                monthly.computeIfAbsent(month, ignored -> new long[2])[foreigner ? 1 : 0] += count;
            }
        }

        long thai = yearly.values().stream().mapToLong(values -> values[0]).sum();
        long foreigner = yearly.values().stream().mapToLong(values -> values[1]).sum();

        List<HistoryItem> monthlyHistory = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            long[] values = monthly.getOrDefault(month, new long[2]);
            monthlyHistory.add(HistoryItem.builder()
                    .label(monthLabel(month))
                    .thai(values[0])
                    .foreigner(values[1])
                    .build());
        }

        List<HistoryItem> yearlyHistory = yearly.entrySet().stream()
                .map(entry -> HistoryItem.builder()
                        .label("ปี " + entry.getKey())
                        .thai(entry.getValue()[0])
                        .foreigner(entry.getValue()[1])
                        .build())
                .toList();

        return VisitStatisticsResponse.builder()
                .thai(thai)
                .foreigner(foreigner)
                .total(thai + foreigner)
                .monthlyStats(periodStatistics(monthlyHistory))
                .yearlyStats(periodStatistics(yearlyHistory))
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
