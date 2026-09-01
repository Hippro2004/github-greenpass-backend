package com.example.greenpass.v1.Stamp.dtos;

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
public class StampResponse {

    private Integer stampId;
    private LocalDate stampDate;
    private LocalTime time;
    private Integer parkId;
    private String parkName;

}
