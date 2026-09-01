package com.example.greenpass.v1.Report.dtos;

import java.time.LocalDate;

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
public class ReportResponse {
    private int reportId;
    private String name;
    private String description;
    private String status;
    private LocalDate reportDate;
    private int parkId;
    private String parkName;
    private String username;
    private String image;
    private String parkRangerName;
}

