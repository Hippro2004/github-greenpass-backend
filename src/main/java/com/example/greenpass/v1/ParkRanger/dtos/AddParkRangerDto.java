package com.example.greenpass.v1.ParkRanger.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddParkRangerDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String position;
    private String startDate;
    private String district;
    private String subDistrict;
    private String province;
    private int gender;
    private String phone;
    private String email;
    private String zipcode;

    private int parkId;

}
