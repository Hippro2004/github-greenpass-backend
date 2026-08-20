package com.example.greenpass.v1.Stamp.dtos;

import java.time.LocalDate;

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
    private Integer parkId;
    private String parkName;

}
