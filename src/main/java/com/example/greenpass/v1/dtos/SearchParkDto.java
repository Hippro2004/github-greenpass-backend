package com.example.greenpass.v1.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParkDto {
    private Integer id;

    private String name;

    private String image;

    private String address;

    private String location;

    private String description;

    private LocalTime openTime;

    private LocalTime closeTime;

    private boolean isSeasonalPark;

    private LocalDate seasonOpenDate;
    private LocalDate seasonCloseDate;

    private boolean isTemporaryClosed;

    private String eventNote;

    private String status;

}
