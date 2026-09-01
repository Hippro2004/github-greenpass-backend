package com.example.greenpass.v1.ParkRanger.dtos;

import com.example.greenpass.v1.Park.entities.Park;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParkRangerResponseDto {

    private String username;
    private String password;
    private String firstname;
    private String surname;
    private String mobilephone;
    private String email;
    private String position;
    private int parkId;
    private String parkName;
    private Park park;

}
