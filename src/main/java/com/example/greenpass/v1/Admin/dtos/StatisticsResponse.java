package com.example.greenpass.v1.Admin.dtos;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsResponse {
    private Map<String, Object> metrics;
    private List<ParkStatDto> parkStats;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParkStatDto {
        private int parkId;
        private String parkName;
        private String province;
        private long announcements;
        private long totalReports;
        private long inProgress;
        private long completed;
    }
}
