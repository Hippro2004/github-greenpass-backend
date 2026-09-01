package com.example.greenpass.v1.Park.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class SearchParkDto {
    private Integer id;
    private Integer parkId;

    private String name;

    private String image;

    private String address;

    private String location;

    private String description;

    private LocalTime openTime;

    private LocalTime closeTime;

    private Boolean isSeasonalPark;

    private LocalDate seasonOpenDate;
    private LocalDate seasonCloseDate;

    private Boolean isTemporaryClosed;

    private String eventNote;

    private String status;

}
