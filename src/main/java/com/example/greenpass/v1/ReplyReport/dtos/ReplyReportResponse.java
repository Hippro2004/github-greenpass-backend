package com.example.greenpass.v1.ReplyReport.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyReportResponse {
    private LocalDate updateDate;
    private LocalTime updateTime;
    private String progress;
    private String currentStatus;
    private String image;
    private String parkRangerName;

}
