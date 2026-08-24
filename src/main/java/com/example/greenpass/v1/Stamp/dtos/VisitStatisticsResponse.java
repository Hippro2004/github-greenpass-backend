package com.example.greenpass.v1.Stamp.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VisitStatisticsResponse {

    private long thai;
    private long foreigner;
    private long total;
    private PeriodStatistics monthlyStats;
    private PeriodStatistics yearlyStats;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PeriodStatistics {
        private long thai;
        private long foreigner;
        private long total;
        private List<HistoryItem> history;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HistoryItem {
        private String label;
        private long thai;
        private long foreigner;
    }
}